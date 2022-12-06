package com.hackathonorganizer.messagingservice.websocket.model.dto;

import com.hackathonorganizer.messagingservice.websocket.model.MessageType;

public record UserSessionDto(

        String videoSessionId,
        String videoSessionToken
) {
}
