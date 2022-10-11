package com.hackathonorganizer.messagingservice.configuration;

import com.hackathonorganizer.messagingservice.websocket.SignalHandler;
import com.hackathonorganizer.messagingservice.websocket.service.MessageService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.*;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Configuration
@EnableWebSocket
@AllArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {

    private final MessageService messageService;

//    @Override
//    public void configureMessageBroker(MessageBrokerRegistry registry) {
//        registry.enableSimpleBroker("/topic");
//        registry.setApplicationDestinationPrefixes("/ws");
//    }
//
//    @Override
//    public void registerStompEndpoints(StompEndpointRegistry registry) {
//        registry.addEndpoint("/messages-websocket")
//                // .setHandshakeHandler(new UserHandshakeHandler())
//                .setAllowedOrigins("*");
//        registry.addEndpoint("/sockjs")
//                .setAllowedOrigins("*")
//                .withSockJS();
//    }


    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new SignalHandler(messageService), "/messages" +
                "-websocket").setAllowedOrigins("*");
    }

//    @Bean
//    public WebSocketHandler signalHandler() {
//        return new SignalHandler();
//    }
//
//    @Bean
//    public ServletServerContainerFactoryBean createWebSocketContainer() {
//        ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
//        container.setMaxTextMessageBufferSize(8192);
//        container.setMaxBinaryMessageBufferSize(8192);
//        return container;
//    }
}
