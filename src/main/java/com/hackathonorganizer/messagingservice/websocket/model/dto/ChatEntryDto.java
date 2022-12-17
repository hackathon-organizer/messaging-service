package com.hackathonorganizer.messagingservice.websocket.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;

import java.time.LocalDateTime;


public record ChatEntryDto (

    String username,
    String entryText,

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonFormat(pattern = "HH:mm dd-MM-yyyy")
    LocalDateTime createdAt
) {
}

