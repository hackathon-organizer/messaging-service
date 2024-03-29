package com.hackathonorganizer.messagingservice.websocket.service;


import com.hackathonorganizer.messagingservice.chat.repository.MessageRepository;
import com.hackathonorganizer.messagingservice.utils.Message;
import com.hackathonorganizer.messagingservice.websocket.model.dto.ChatEntryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;

    public ChatEntryDto saveChatMessage(Message message) {

        message.setCreatedAt(LocalDateTime.now());
        Message savedMessage = messageRepository.save(message);

        return mapToDto(savedMessage);
    }

    private ChatEntryDto mapToDto(Message message) {

        return new ChatEntryDto(
                message.getUsername(),
                message.getEntryText(),
                message.getCreatedAt());
    }
}
