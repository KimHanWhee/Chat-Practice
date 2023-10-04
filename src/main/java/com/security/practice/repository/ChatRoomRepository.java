package com.security.practice.repository;

import com.security.practice.domain.entity.ChatRoom;
import com.security.practice.domain.entity.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    Optional<ChatRoom> findByName(String chatRoomName);

    Page<ChatRoom> findByNameContaining(String chatRoomName, Pageable pageable);
}
