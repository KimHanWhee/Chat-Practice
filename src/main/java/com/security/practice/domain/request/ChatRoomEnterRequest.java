package com.security.practice.domain.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ChatRoomEnterRequest {
    private String chatRoomName;
    private String userName;
    private String senderId;
    private String message;
}
