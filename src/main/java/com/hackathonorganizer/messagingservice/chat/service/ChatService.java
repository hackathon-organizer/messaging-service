package com.hackathonorganizer.messagingservice.chat.service;

import com.hackathonorganizer.messagingservice.chat.repository.MessageRepository;
import com.hackathonorganizer.messagingservice.exception.MessagingException;
import com.hackathonorganizer.messagingservice.utils.RestCommunicator;
import com.hackathonorganizer.messagingservice.utils.dto.UserResponseDto;
import com.hackathonorganizer.messagingservice.utils.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {

    private final MessageRepository messageRepository;

    private final RestCommunicator restCommunicator;

    public List<Message> getChatRoomMessages(Long roomId, Jwt principal) {

        UserResponseDto userResponseDto = restCommunicator.getUserDetails(principal.getClaim("sub"));

        if (principal.getClaim("realm_access").toString().contains("MENTOR") || userResponseDto.currentTeamId().equals(roomId)) {
            return messageRepository.findMessagesByTeamId(roomId);
        } else {
            throw new MessagingException("User is not team member. Can't get chat messages.", HttpStatus.FORBIDDEN);
        }
    }
}
