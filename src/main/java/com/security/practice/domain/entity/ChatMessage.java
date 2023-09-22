package com.security.practice.domain.entity;

import com.security.practice.domain.request.ChatMessageRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_idx")
    private Users sender;

    private String content;

    private LocalDateTime writeTime;

    public ChatMessage(ChatRoom chatRoom, Users sender, ChatMessageRequest chatMessageRequest) {
        this.chatRoom = chatRoom;
        this.sender = sender;
        this.content = chatMessageRequest.getMessage();
    }

    public ChatMessage(long chatMessageId) {
        this.id = chatMessageId;
    }


    @PrePersist
    public void prePersist() {
        this.writeTime = LocalDateTime.now();
    }
}
