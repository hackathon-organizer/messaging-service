package com.hackathonorganizer.messagingservice.chat.controller;

import com.hackathonorganizer.messagingservice.chat.service.ChatService;
import com.hackathonorganizer.messagingservice.utils.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@RestController
@RequestMapping("/api/v1/messages")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @GetMapping("/rooms/{roomId}")
    @RolesAllowed("USER")
    public List<Message> getChatMessages(@PathVariable("roomId") Long roomId, @AuthenticationPrincipal Jwt jwt) {
        return chatService.getChatRoomMessages(roomId, jwt);
    }
}
