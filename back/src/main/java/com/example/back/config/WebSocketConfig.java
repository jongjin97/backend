package com.example.back.config;


import com.example.back.handler.SocketHandler;
import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.socket.config.annotation.*;

import java.util.Objects;

/**
 * 1:1 채팅 기능에 필요한 웹소켓
 */

//@Configuration
//@EnableWebSocket
//@RequiredArgsConstructor
//public class WebSocketConfig implements WebSocketConfigurer {
//
//    private final SocketHandler socketHandler;
//
//    @Override
//    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
//        registry.addHandler(socketHandler, "/chating");
//    }
//}
@Configuration
@RequiredArgsConstructor
@EnableWebSocket
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    private final JwtProvider jwtProvider;
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // 메시지 보낼 받을 때 경로
        config.enableSimpleBroker("/sub");
        // 메시지 보낼 때 경로
        config.setApplicationDestinationPrefixes("/room");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Stomp Socket 엔드포인트설정
        registry.addEndpoint("/ws").setAllowedOriginPatterns("*").withSockJS();
    }

    //Socket token 확인
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor =
                        MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
                // Socket 처음 연결 시
                if (StompCommand.CONNECT.equals(accessor.getCommand())) {
                    String token = Objects.requireNonNull(accessor.getNativeHeader("Authorization")).get(0);
                    if(jwtProvider.validateToken(token)){
                        token = token.split(" ")[1].trim();
                        Authentication user = jwtProvider.getAuthentication(token);

                        accessor.setUser(user);
                    }
                }
                return message;
            }
        });
    }
}