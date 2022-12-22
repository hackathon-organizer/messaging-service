package com.hackathonorganizer.messagingservice.chat.service;

import com.hackathonorganizer.messagingservice.chat.repository.MessageRepository;
import com.hackathonorganizer.messagingservice.exception.MessagingException;
import com.hackathonorganizer.messagingservice.utils.RestCommunicator;
import com.hackathonorganizer.messagingservice.utils.dto.UserResponseDto;
import com.hackathonorganizer.messagingservice.utils.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {

    private final MessageRepository messageRepository;

    private final RestCommunicator restCommunicator;

    public List<Message> getChatRoomMessages(Long roomId, Principal principal) {

        UserResponseDto userResponseDto = restCommunicator.getUserDetails(principal.getName());

        if (userResponseDto.currentTeamId().equals(roomId)) {
            return messageRepository.findMessagesByTeamId(roomId);
        } else {
            throw new MessagingException("User is not team member. Can't get chat messages.", HttpStatus.FORBIDDEN);
        }
    }
}
