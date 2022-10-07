package com.hackathonorganizer.messagingservice.websocket.service;


import com.hackathonorganizer.messagingservice.chat.model.Message;
import com.hackathonorganizer.messagingservice.chat.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Setter
@Slf4j
public class MessageService {

    private final SimpMessagingTemplate messagingTemplate;

    private final MessageRepository messageRepository;

    private final SimpUserRegistry simpUserRegistry;


    public void saveChatMessage(String roomId, Message message) {

        message.setCreatedAt(LocalDateTime.now());
        message.setChatId(Long.parseLong(roomId));
        message.setUserId(message.getUserId());

        messageRepository.save(message);
    }
}
