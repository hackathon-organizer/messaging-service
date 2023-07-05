package com.hackathonorganizer.messagingservice.utils;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Entity
@Table(name = "chat_entry")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NotEmpty(message = "Message can not be empty!")
    String entryText;

    @NotEmpty(message = "Username can not be empty!")
    String username;

    @NotNull
    Long teamId;

    @NotNull
    Long userId;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonFormat(pattern = "HH:mm dd-MM-yyyy")
    @NotNull
    LocalDateTime createdAt;
}
