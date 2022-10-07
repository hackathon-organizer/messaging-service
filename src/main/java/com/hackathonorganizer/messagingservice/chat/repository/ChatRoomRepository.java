package com.hackathonorganizer.messagingservice.chat.repository;

import com.hackathonorganizer.messagingservice.chat.model.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

}