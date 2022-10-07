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

//    private final NotificationService notificationService;
//
//    @MessageMapping("/private-message")
//    @SendToUser("/topic/private-messages")
//    public void inviteUserToTeam(TeamInvitationDto teamInvitation) {
//
//        notificationService.sendTeamInviteNotification(teamInvitation);
//
//        log.info("User id: {} send invite to team", NotificationService.userId);
//    }

    @MessageMapping("/room/{roomId}")
    public Message getMessage(@DestinationVariable String roomId, @Payload Message message) {

        messageService.saveChatMessage(roomId, message);

        return message;
    }

}
