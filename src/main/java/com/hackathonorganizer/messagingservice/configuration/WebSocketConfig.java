package com.hackathonorganizer.messagingservice.configuration;

import com.hackathonorganizer.messagingservice.websocket.service.MessageService;
import com.hackathonorganizer.messagingservice.websocket.service.SignalHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {

    private final MessageService messageService;
    private final VaultProperties vaultProperties;

    @Value("${ws.origins}")
    private String origins;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {

        registry.addHandler(new SignalHandler(vaultProperties, messageService),
                        "/messages-websocket")
                .setAllowedOrigins(origins);
    }
}
