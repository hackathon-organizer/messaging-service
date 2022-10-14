package com.hackathonorganizer.messagingservice.websocket.service;


import com.hackathonorganizer.messagingservice.websocket.model.Message;
import com.hackathonorganizer.messagingservice.chat.repository.MessageRepository;
import com.hackathonorganizer.messagingservice.websocket.model.ChatEntry;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Setter
@Slf4j
public class MessageService {


    private final MessageRepository messageRepository;



    public ChatEntry saveChatMessage(Message message) {

        message.setCreatedAt(LocalDateTime.now());

        Message savedMessage = messageRepository.save(message);

        return mapToDto(savedMessage);
    }

    private ChatEntry mapToDto(Message message) {
        return ChatEntry.builder()
                .username(message.getUsername())
                .entryText(message.getEntryText())
                .createdAt(message.getCreatedAt())
                .build();
    }
}
