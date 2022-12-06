package com.hackathonorganizer.messagingservice.websocket.model;

import lombok.*;
import org.springframework.web.socket.WebSocketSession;

@RequiredArgsConstructor
@Getter
@Setter
@ToString
@AllArgsConstructor
public class UserSession {

    private final String username;
    private final WebSocketSession session;

    private String videoSessionId;

    private String videoSessionToken;
}
