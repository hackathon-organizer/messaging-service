package com.hackathonorganizer.messagingservice.chat.repository;

import com.hackathonorganizer.messagingservice.chat.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

   @Query("SELECT m FROM Message m WHERE m.chatId = :roomId")
    List<Message> findMessagesByChatId(Long roomId);
}