package com.hackathonorganizer.messagingservice.chat.controller;

import com.hackathonorganizer.messagingservice.exception.SignalServerException;
import com.hackathonorganizer.messagingservice.websocket.model.Message;
import com.hackathonorganizer.messagingservice.chat.service.ChatService;
import io.openvidu.java.client.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.annotation.security.RolesAllowed;
import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/messages")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @GetMapping("/rooms/{roomId}")
    @RolesAllowed({"USER", "MENTOR", "ORGANIZER"})
    public List<Message> getChatMessages(@PathVariable("roomId") Long roomId, Principal principal) {

        return chatService.getChatRoomMessages(roomId, principal);
    }
}
