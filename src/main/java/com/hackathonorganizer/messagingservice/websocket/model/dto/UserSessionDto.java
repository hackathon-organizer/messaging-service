package com.hackathonorganizer.messagingservice.websocket.model.dto;

public record UserSessionDto(

        String videoSessionId,
        String videoSessionToken
) {
}
