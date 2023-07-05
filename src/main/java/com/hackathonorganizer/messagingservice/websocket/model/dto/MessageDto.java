package com.hackathonorganizer.messagingservice.websocket.model.dto;

import com.hackathonorganizer.messagingservice.utils.MessageType;

public record MessageDto(

        MessageType messageType,
        Object data
) {
}
