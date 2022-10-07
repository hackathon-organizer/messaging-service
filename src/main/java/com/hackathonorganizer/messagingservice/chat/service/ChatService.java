package com.hackathonorganizer.messagingservice.chat.service;

import com.hackathonorganizer.messagingservice.chat.model.ChatRoom;
import com.hackathonorganizer.messagingservice.chat.model.Message;
import com.hackathonorganizer.messagingservice.chat.repository.ChatRoomRepository;
import com.hackathonorganizer.messagingservice.chat.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final MessageRepository messageRepository;

    public Long createTeamChat(Long teamId) {

        ChatRoom room = ChatRoom.builder().teamId(teamId).build();

        log.info("Chat room for team with id: " + teamId + " created successfully");

        return chatRoomRepository.save(room).getId();
    }

    public List<Message> getChatRoomMessages(Long roomId) {

        List<Message> roomMessages =
                messageRepository.findMessagesByChatId(roomId);

        return roomMessages;
    }
}
