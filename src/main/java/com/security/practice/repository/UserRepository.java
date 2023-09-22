package com.security.practice.repository;

import com.security.practice.domain.entity.ChatRoom;
import com.security.practice.domain.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByUserId(String userId);

    Optional<Users> findByUserName(String senderName);
}
