package com.hackathonorganizer.messagingservice.websocket.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Builder
@Setter
public class ChatEntry {

    String entryText;
    String username;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonFormat(pattern = "HH:mm dd-MM-yyyy")
    LocalDateTime createdAt;
}

