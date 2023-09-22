package com.security.practice.repository;

import com.security.practice.domain.entity.ChatMessage;
import com.security.practice.domain.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    Optional<ChatMessage> findByChatRoom(ChatRoom chatRoom);
}
