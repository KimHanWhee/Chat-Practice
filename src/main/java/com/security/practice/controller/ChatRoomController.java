package com.security.practice.controller;

import com.security.practice.domain.entity.ChatRoom;
import com.security.practice.domain.reponse.ChatRoomResponse;
import com.security.practice.domain.request.ChatRoomRequest;
import com.security.practice.exception.AlreadyExistException;
import com.security.practice.exception.NotExitedChatRoom;
import com.security.practice.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomService chatRoomService;
    @PostMapping
    public ResponseEntity<String> createRoom(@RequestBody ChatRoomRequest request){
        System.out.println(request.getChatRoomName());
        return chatRoomService.createChatRoom(request);
    }

    @GetMapping
    public Page<ChatRoomResponse> myRooms(@PageableDefault(size = 5) Pageable pageable){
        return chatRoomService.getMyRooms(pageable);
    }
    @GetMapping("/all")
    public Page<ChatRoomResponse> getAllRooms(@PageableDefault(size=5) Pageable pageable){
        return chatRoomService.getAllRooms(pageable);
    }

    @PostMapping("/join")
    public ResponseEntity<ChatRoom> joinRoom(@RequestBody HashMap<String, String> request) throws NotExitedChatRoom, AlreadyExistException {
        String chatRoomName = request.get("chatRoomName");
        return chatRoomService.joinRoom(chatRoomName);
    }

}
