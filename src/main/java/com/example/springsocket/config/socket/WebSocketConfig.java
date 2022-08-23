package com.example.springsocket.config.socket;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration // 해당 클래스가 Bean의 설정을 할 것이라는 것을 나타냄.
@EnableWebSocketMessageBroker // websocket server를 활성화
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final StompHandler stompHandler;

    // 1. 클라이언트가 웹 소켓 서버에 연결하는 데 사용할 웹 소켓 엔드포인트.
    // 엔드포인트 구성에 withSockJS 사용 -> 웹 소켓을 지원하지 않는 브라우저에 fallback 옵션 활성화.
    // *fallback : 어떤 기능이 약해지거나 제대로 동작하지 않을 때, 이에 대처하는 기능 또는 동작
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws/chat")
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }

    // 2. 한 클라이언트에서 다른 클라이언트로 메시지를 라우팅하는 데 사용될 메시지 브로커를 구성.
    // /app으로 시작하는 모든 메시지는 @MessageMapping 어노테이션이 달린 메서드로 라우팅.
    // "/topic"으로 시작하는 메시지가 메시지 브로커로 라우팅되도록 정의. 메시지브로커는 해당 채팅방을 구독하고 있는 클라이언트에게 메시지 전달
    // queue는 주로 1대1 메시징, topic은 주로 1대 다 메시징일 때 사용.
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
       registry.setApplicationDestinationPrefixes("/app"); // pub
       registry.enableSimpleBroker("/topic","/queue"); // sub
    }

    // websocket 앞단에서 jwt 토큰을 검증할 수 있도록 stompHander를 interceptor로 추가.
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(stompHandler);
    }
}
