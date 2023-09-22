package com.security.practice.domain.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.socket.TextMessage;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ChatRoomRequest {
    private String chatRoomName;

}
