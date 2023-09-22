package com.security.practice.domain.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.security.practice.domain.request.ChatRoomRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_room_id")
    private Long id;

    @Column(unique = true)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_idx")
    private Users creator;

    @JsonBackReference
    @ManyToMany(mappedBy = "participantChatRooms")
    private List<Users> participants = new ArrayList<>();


    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL)
    private List<ChatMessage> messages;

    private LocalDateTime createdAt;

    public ChatRoom(ChatRoomRequest chatRoomRequest, Users users) {
        this.creator = users;
        this.name = chatRoomRequest.getChatRoomName();
    }

    public ChatRoom(Long chatRoomId) {
        this.id = chatRoomId;
    }

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}
