package com.hackathonorganizer.messagingservice.websocket.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.springframework.web.socket.WebSocketSession;

import java.util.Objects;

@AllArgsConstructor
@Getter
@Setter
public class UserSession {

    String username;
    WebSocketSession session;
}
