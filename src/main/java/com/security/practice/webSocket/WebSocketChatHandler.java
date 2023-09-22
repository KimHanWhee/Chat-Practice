package com.security.practice.webSocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.security.practice.domain.dto.ChatMessageDTO;
import com.security.practice.domain.request.ChatRoomRequest;
import com.security.practice.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Log4j2
@Component
@RequiredArgsConstructor
public class WebSocketChatHandler extends TextWebSocketHandler {
    private final ObjectMapper mapper;

    private final ChatRoomService chatService;

    private final Set<WebSocketSession> sessions = new HashSet<>();

    private final Map<Long, Set<WebSocketSession>> chatRoomSessionMap = new HashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception{
        log.info("{} 연결됨", session.getId());
        sessions.add(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws JsonProcessingException {
        String payload = message.getPayload();
        try{
            ChatRoomRequest chatRoomRequest = mapper.readValue(payload, ChatRoomRequest.class);
            chatService.createChatRoom(chatRoomRequest);
            log.info("payload {}", payload);
            log.info(mapper.readValue(payload, ChatMessageDTO.class).toString());

            ChatMessageDTO chatMessageDTO = mapper.readValue(payload, ChatMessageDTO.class);
            log.info("session {}", chatMessageDTO.toString());

            Long chatRoomId = chatMessageDTO.getChatRoomId();

            if(!chatRoomSessionMap.containsKey(chatRoomId)){
                chatRoomSessionMap.put(chatRoomId, new HashSet<>());
            }

            Set<WebSocketSession> chatRoomSession = chatRoomSessionMap.get(chatRoomId);

            if(chatMessageDTO.getMessageType().equals(ChatMessageDTO.MessageType.ENTER)) chatRoomSession.add(session);
            if(chatRoomSession.size() >= 3) removeClosedSession(chatRoomSession);
            sendMessageToChatRoom(chatMessageDTO, chatRoomSession);
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception{
        log.info("{} 연결 끊김", session.getId());
        sessions.remove(session);
    }

    private void removeClosedSession(Set<WebSocketSession> chatRoomSession) {
        chatRoomSession.removeIf(sess -> !sessions.contains(sess));
    }

    private void sendMessageToChatRoom(ChatMessageDTO chatMessageDto, Set<WebSocketSession> chatRoomSession) {
        chatRoomSession.parallelStream().forEach(sess -> sendMessage(sess, chatMessageDto));//2
    }


    public <T> void sendMessage(WebSocketSession session, T message) {
        try{
            session.sendMessage(new TextMessage(mapper.writeValueAsString(message)));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }
}
