package com.security.practice.domain.dto;

import lombok.*;

@ToString
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageDTO {
    public enum MessageType{
        ENTER, TALK
    }

    private MessageType messageType;
    private Long chatRoomId;
    private Long SenderId;
    private String message;
}
