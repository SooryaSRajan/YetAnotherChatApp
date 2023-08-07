package com.ssrprojects.ultimatechatapp.config.security;

import com.ssrprojects.ultimatechatapp.entity.User;
import com.ssrprojects.ultimatechatapp.entity.enums.Roles;
import com.ssrprojects.ultimatechatapp.service.UserService.UserService;
import com.ssrprojects.ultimatechatapp.utils.TokenGenerator;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.util.ObjectUtils.isEmpty;

@Component
public class JWTUtil {

    public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;
    private static final String USER_SESSION_TOKEN = "sessionToken";

    @Value("${ultimatechatapp.jwt.secret}")
    private String secret;
    private final UserService userService;

    public JWTUtil(UserService userService) {
        this.userService = userService;
    }

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }

    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts
                .builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Claims getAllClaimsFromToken(String token) {
        try {
            return Jwts
                    .parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            return null;
        }
    }

    public String generateToken(Authentication authentication) {
        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();

        Map<String, Object> claims = new HashMap<>();
        Collection<? extends GrantedAuthority> roles = userPrincipal.getAuthorities();
        if (roles.contains(Roles.ADMIN)) {
            claims.put(Roles.ADMIN.getAuthority(), true);
        }
        if (roles.contains(Roles.USER)) {
            claims.put(Roles.USER.getAuthority(), true);
        }

        User user = userService.getUserByUsername(userPrincipal.getUsername());
        String sessionToken = user.getActiveSessionToken();

        if (isEmpty(sessionToken)) {
            sessionToken = TokenGenerator.generateToken(16);
            user.setActiveSessionToken(sessionToken);
            userService.saveOrUpdateUser(user);
        }

        claims.put(USER_SESSION_TOKEN, sessionToken);

        return doGenerateToken(claims, userPrincipal.getUsername());
    }

    public boolean validateToken(String token) {
        Claims claims = getAllClaimsFromToken(token);

        if (claims == null) {
            return false;
        }

        String username = claims.getSubject();
        User user = userService.getUserByUsername(username);
        String sessionToken = claims.get(USER_SESSION_TOKEN, String.class);

        boolean isTokenValid = !claims.getExpiration().before(new Date())
                && (claims.get(Roles.ADMIN.getAuthority(), Boolean.class) != null
                || claims.get(Roles.USER.getAuthority(), Boolean.class) != null);

        return isTokenValid && !isEmpty(sessionToken) && sessionToken.equals(user.getActiveSessionToken());
    }

    public String getUsernameFromToken(String token) {
        Claims claims = getAllClaimsFromToken(token);
        return claims != null ? claims.getSubject() : null;
    }


}
