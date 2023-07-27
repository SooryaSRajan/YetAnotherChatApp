package com.ssrprojects.ultimatechatapp.config.security;

import com.datastax.oss.driver.shaded.guava.common.net.HttpHeaders;
import com.ssrprojects.ultimatechatapp.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import reactor.util.annotation.NonNull;

import java.io.IOException;
import java.util.List;

import static org.springframework.util.ObjectUtils.isEmpty;

@Component
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    UserRepository userRepository;
    JWTUtil jwtTokenUtil;

    public TokenAuthenticationFilter(UserRepository userRepository, JWTUtil jwtTokenUtil) {
        this.userRepository = userRepository;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        // Get authorization header and validate
        final String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (isEmpty(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        // Get jwt token and validate
        if (!jwtTokenUtil.validateToken(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        // Get user identity and set it on the spring security context
        UserDetails userDetails = userRepository
                .findByUsername(jwtTokenUtil.getUsernameFromToken(token))
                .orElse(null);

        UsernamePasswordAuthenticationToken
                authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null,
                userDetails == null ?
                        List.of() : userDetails.getAuthorities()
        );

        authentication.setDetails(
                new WebAuthenticationDetailsSource().buildDetails(request)
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);

    }
}


