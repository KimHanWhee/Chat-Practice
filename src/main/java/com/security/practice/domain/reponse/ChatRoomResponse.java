package com.security.practice.domain.reponse;

import com.security.practice.domain.entity.ChatRoom;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ChatRoomResponse {
    private Long chatRoomId;
    private String chatRoomName;
    private String creatorName;
    private LocalDateTime createdAt;

    public ChatRoomResponse(ChatRoom el) {
        this.chatRoomId = el.getId();
        this.chatRoomName = el.getName();
        this.creatorName = el.getCreator().getUserName();
        this.createdAt = el.getCreatedAt();
    }
}
