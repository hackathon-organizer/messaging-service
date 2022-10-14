package com.hackathonorganizer.messagingservice.chat.controller;

import com.hackathonorganizer.messagingservice.websocket.model.Message;
import com.hackathonorganizer.messagingservice.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/messages")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @PostMapping
    public Long createTeamChatRoom(@RequestBody Long teamId) {
        return chatService.createTeamChat(teamId);
    }

    @GetMapping("/rooms/{roomId}")
    public List<Message> getChatRoomMessages(@PathVariable("roomId") Long roomId) {
        return chatService.getChatRoomMessages(roomId);
    }

}
