package com.security.practice.service;

import com.security.practice.domain.dto.ChatMessageDTO;
import com.security.practice.domain.entity.ChatMessage;
import com.security.practice.domain.entity.ChatRoom;
import com.security.practice.domain.entity.Users;
import com.security.practice.domain.reponse.ChatMessageResponse;
import com.security.practice.domain.request.ChatMessageRequest;
import com.security.practice.repository.ChatMessageRepository;
import com.security.practice.repository.ChatRoomRepository;
import com.security.practice.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;


    @Transactional
    public List<ChatMessage> findMessage(Long chatRoomId){
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 채팅방입니다."));
        return chatRoom.getMessages();
    }

    @Transactional
    public ChatMessageResponse sendMessage(Long chatRoomId, ChatMessageRequest chatMessageRequest){
        Users sender = userRepository.findByUserName(chatMessageRequest.getSenderName()).orElseThrow(NullPointerException::new);
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 채팅방입니다."));
        ChatMessage chatMessage = new ChatMessage(chatRoom, sender, chatMessageRequest);
        chatMessageRepository.save(chatMessage);
        return new ChatMessageResponse(chatMessage);
    }

    @Transactional
    public void deleteMessage(long chatMessageId){
        ChatMessage chatMessage = new ChatMessage(chatMessageId);
        chatMessageRepository.delete(chatMessage);
    }
}
