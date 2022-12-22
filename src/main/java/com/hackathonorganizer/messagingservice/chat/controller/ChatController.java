package com.hackathonorganizer.messagingservice.chat.controller;

import com.hackathonorganizer.messagingservice.chat.service.ChatService;
import com.hackathonorganizer.messagingservice.utils.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/messages")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @GetMapping("/rooms/{roomId}")
    @RolesAllowed({"USER"})
    public List<Message> getChatMessages(@PathVariable("roomId") Long roomId, Principal principal) {

        return chatService.getChatRoomMessages(roomId, principal);
    }
}
