package com.security.practice.controller;

import com.security.practice.domain.entity.ChatMessage;
import com.security.practice.domain.entity.ChatRoom;
import com.security.practice.domain.entity.Users;
import com.security.practice.domain.reponse.ChatMessageResponse;
import com.security.practice.domain.request.ChatMessageRequest;
import com.security.practice.domain.request.ChatRoomEnterRequest;
import com.security.practice.exception.AlreadyExistException;
import com.security.practice.exception.NotExitedChatRoom;
import com.security.practice.exception.TokenException;
import com.security.practice.service.ChatMessageService;
import com.security.practice.service.ChatRoomService;
import com.security.practice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.broker.SimpleBrokerMessageHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@Slf4j
@RequiredArgsConstructor
public class ChatController {
    private final ChatRoomService chatRoomService;
    private final ChatMessageService chatMessageService;
    private final UserService userService;
    private final SimpMessageSendingOperations simpMessageSendingOperations;

    @MessageMapping("/enter")
    public void enter(ChatRoomEnterRequest message) {
        Long chatRoomId = chatRoomService.getChatRoomByName(message.getChatRoomName());
        message.setMessage(message.getUserName() + "님의 등장!!!");
        simpMessageSendingOperations.convertAndSend("/sub/chat/room/" + chatRoomId, message);
    }

    @MessageMapping("/chat/{chatRoomId}")
    public void sendMessage(@DestinationVariable("chatRoomId") Long chatRoomId, ChatMessageRequest message){
        simpMessageSendingOperations.convertAndSend("/sub/chat/room/" + chatRoomId, message);
    }
}
