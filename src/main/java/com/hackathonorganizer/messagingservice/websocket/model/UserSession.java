package com.hackathonorganizer.messagingservice.websocket.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.socket.WebSocketSession;

@AllArgsConstructor
@Getter
@Setter
public class UserSession {

    String username;
    WebSocketSession session;
}
