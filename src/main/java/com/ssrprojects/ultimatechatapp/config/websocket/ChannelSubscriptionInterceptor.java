package com.ssrprojects.ultimatechatapp.config.websocket;

import com.ssrprojects.ultimatechatapp.config.security.JWTUtil;
import com.ssrprojects.ultimatechatapp.service.UserService.UserService;
import org.springframework.lang.NonNull;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.List;

import static org.springframework.util.ObjectUtils.isEmpty;

@Component
public class ChannelSubscriptionInterceptor implements ChannelInterceptor {

    final JWTUtil jwtUtil;
    final UserService userService;

    public ChannelSubscriptionInterceptor(JWTUtil jwtUtil, UserService userService) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @Override
    public Message<?> preSend(@NonNull Message<?> message, @NonNull MessageChannel channel) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(message);
        final StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        Authentication authenticatedUser = null;

        List<String> authorizationHeader = headerAccessor.getNativeHeader("Authorization");

        if (authorizationHeader != null) {
            authenticatedUser = authenticateUser(authorizationHeader.get(0));
            if (accessor != null && authenticatedUser != null) {
                accessor.setUser(authenticatedUser);
            } else {
                throw new MessagingException("Invalid token");
            }
        }

        if (StompCommand.SUBSCRIBE.equals(headerAccessor.getCommand())) {
            if (!validateSubscription(authenticatedUser, headerAccessor.getDestination())) {
                throw new MessagingException("User not authorized to subscribe to this channel");
            }
        }

        return ChannelInterceptor.super.preSend(message, channel);
    }

    private boolean validateSubscription(Principal user, String channel) {
        if (user == null) {
            return false;
        }

        return !channel.startsWith("/user") || channel.startsWith("/user/" + user.getName() + "/");
    }

    public Authentication authenticateUser(String token) {
        try {

            if (isEmpty(token) || !token.startsWith("Bearer ")) {
                return null;
            }

            token = token.split(" ")[1].trim();
            if (!jwtUtil.validateToken(token)) {
                return null;
            }

            UsernamePasswordAuthenticationToken authentication = userService
                    .getAuthenticationToken(jwtUtil.getUsernameFromToken(token));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            return authentication;
        } catch (Exception e) {
            return null;
        }
    }
}
