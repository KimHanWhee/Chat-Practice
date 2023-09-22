package com.security.practice.domain.reponse;

import com.security.practice.domain.entity.ChatMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ChatMessageResponse {
    private Long senderId;
    private String senderName;
    private String message;

    public ChatMessageResponse(ChatMessage chatMessage) {
        this.senderId = chatMessage.getSender().getId();
        this.senderName = chatMessage.getSender().getUserName();
        this.message = chatMessage.getContent();
    }
}
