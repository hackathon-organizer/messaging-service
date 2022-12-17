package com.hackathonorganizer.messagingservice.chat.repository;

import com.hackathonorganizer.messagingservice.websocket.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findMessagesByTeamId(Long teamId);
}