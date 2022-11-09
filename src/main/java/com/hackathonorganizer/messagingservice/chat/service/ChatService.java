package com.hackathonorganizer.messagingservice.chat.service;

import com.hackathonorganizer.messagingservice.chat.model.ChatRoom;
import com.hackathonorganizer.messagingservice.websocket.model.Message;
import com.hackathonorganizer.messagingservice.chat.repository.ChatRoomRepository;
import com.hackathonorganizer.messagingservice.chat.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final MessageRepository messageRepository;


    public Long createTeamChat(Long teamId) {

        ChatRoom room = ChatRoom.builder().teamId(teamId).build();

        ChatRoom savedChatRoom = chatRoomRepository.save(room);

        log.info("Chat room for team with id: {} created successfully", savedChatRoom.getId());

        return savedChatRoom.getId();
    }

    public List<Message> getChatRoomMessages(Long roomId) {

        return messageRepository.findMessagesByChatId(roomId);
    }

}
