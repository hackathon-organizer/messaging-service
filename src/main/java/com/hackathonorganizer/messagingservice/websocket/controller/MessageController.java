package com.hackathonorganizer.messagingservice.websocket.controller;

import com.hackathonorganizer.messagingservice.chat.model.Message;
import com.hackathonorganizer.messagingservice.websocket.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MessageController {

    private final MessageService messageService;

//    @MessageMapping("/room/{roomId}")
//    public Message getMessage(@DestinationVariable String roomId, @Payload Message message) {
//
//        messageService.saveChatMessage(message);
//
//        return message;
//    }

}
