package com.security.practice.service;

import com.security.practice.domain.entity.ChatRoom;
import com.security.practice.domain.entity.Users;
import com.security.practice.domain.reponse.ChatRoomResponse;
import com.security.practice.domain.request.ChatRoomRequest;
import com.security.practice.exception.AlreadyExistException;
import com.security.practice.exception.NotExitedChatRoom;
import com.security.practice.exception.TokenException;
import com.security.practice.repository.ChatMessageRepository;
import com.security.practice.repository.ChatRoomRepository;
import com.security.practice.repository.UserRepository;
import com.security.practice.security.JwtService;
import com.security.practice.util.ChangeListToPage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.antlr.v4.runtime.Token;
import org.apache.catalina.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.objenesis.instantiator.basic.NullInstantiator;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChangeListToPage changeListToPage;
    private final UserRepository userRepository;
    private final JwtService jwtService;


    @Transactional
    public ChatRoom findChatRoom(long id){
        return chatRoomRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 채팅방 입니다."));
    }
    @Transactional
    public ResponseEntity<String> createChatRoom(ChatRoomRequest chatRoomRequest){
        try {
            Long userId = jwtService.tokenToDTO(jwtService.getAccessToken()).getId();
            Users users = userRepository.findById(userId).orElseThrow(NullPointerException::new);
            ChatRoom chatRoom = new ChatRoom(chatRoomRequest, users);
            users.getParticipantChatRooms().add(chatRoom);
            chatRoom.getParticipants().add(users);
            chatRoomRepository.save(chatRoom);
            return new ResponseEntity<>("채팅방이 생성되었습니다.", HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>("채팅방 생성에 실패하였습니다.", HttpStatus.BAD_REQUEST);
        }
    }

    @Transactional
    public ChatRoom update(Long id, ChatRoomRequest request){
        ChatRoom findById = chatRoomRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 채팅방 입니다."));
        return chatRoomRepository.save(findById);
    }

    public Page<ChatRoomResponse> getMyRooms(Pageable pageable) {
        try{
            Long userId = jwtService.tokenToDTO(jwtService.getAccessToken()).getId();
            List<ChatRoom> chatRoomList = userRepository.findById(userId).orElseThrow(NullPointerException::new).getParticipantChatRooms();
            chatRoomList.sort((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()));
            Page<ChatRoom> chatRoomPage = changeListToPage.ConvertToPage(chatRoomList, pageable.getPageNumber(), pageable.getPageSize());
            return chatRoomPage.map(ChatRoomResponse::new);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public Page<ChatRoomResponse> getAllRooms(Pageable pageable) {
        return chatRoomRepository.findAll(pageable).map(ChatRoomResponse::new);
    }

    @Transactional
    public ResponseEntity<ChatRoom> joinRoom(String chatRoomName) throws NotExitedChatRoom, AlreadyExistException {
        try {
            Long userId = jwtService.tokenToDTO(jwtService.getAccessToken()).getId();
            log.info(userId);
            ChatRoom chatRoom = chatRoomRepository.findByName(chatRoomName).orElseThrow(NullPointerException::new);
            log.info(chatRoom.getId());
            Users users = userRepository.findById(userId).orElseThrow(TokenException::new);
            if(chatRoom.getParticipants().contains(users)){
                log.info("얘 있는애임");
                throw new AlreadyExistException();
            }
            users.getParticipantChatRooms().add(chatRoom);
            chatRoom.getParticipants().add(users);
            return ResponseEntity.ok(new ChatRoom(chatRoom.getId()));
        }catch (AlreadyExistException e){
            throw new AlreadyExistException();
        }catch (Exception e){
            throw new NotExitedChatRoom();
        }
    }

    public Long getChatRoomByName(String chatRoomName) {
        return chatRoomRepository.findByName(chatRoomName).orElseThrow().getId();
    }


    public Page<ChatRoom> searchChatRoom(String chatRoomName, Pageable pageable) {
        return chatRoomRepository.findByNameContaining(chatRoomName, pageable);
    }
}
