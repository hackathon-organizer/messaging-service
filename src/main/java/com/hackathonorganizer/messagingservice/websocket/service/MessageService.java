package com.hackathonorganizer.messagingservice.websocket.service;


import com.hackathonorganizer.messagingservice.chat.model.Message;
import com.hackathonorganizer.messagingservice.chat.repository.MessageRepository;
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



    public void saveChatMessage(Message message) {

        message.setCreatedAt(LocalDateTime.now());

        messageRepository.save(message);
    }
}
