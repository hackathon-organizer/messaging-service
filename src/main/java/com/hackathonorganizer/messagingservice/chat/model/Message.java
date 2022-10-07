package com.hackathonorganizer.messagingservice.chat.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "chat_entry")
@Getter
@Setter
@ToString
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String entryText;

    String username;

    Long chatId;

    Long userId;

    LocalDateTime createdAt;
}
