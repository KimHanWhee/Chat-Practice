package com.security.practice.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.security.practice.domain.request.SignUpRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Users {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_idx")
    private Long id;
    @Column(unique = true)
    private String userId;
    private String userPw;
    private String userName;
    private String userBirth;
    private String sleepYn;
    private String delYn;
    @OneToMany(mappedBy = "creator")
    private List<ChatRoom> ownerChatRooms;
    @OneToMany(mappedBy = "sender")
    private List<ChatMessage> chatMessages;

    @JsonManagedReference
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "user_chat_room",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "chat_room_id")
    )
    private List<ChatRoom> participantChatRooms = new ArrayList<>();

    public Users(Long userId) {
        this.id = userId;
    }


    @PrePersist
    public void prePersist(){
        this.sleepYn = "N";
        this.delYn = "N";
    }

    public Users(SignUpRequest request, String encodedPassword) {
        this.userId = request.getUserId();
        this.userPw = encodedPassword;
        this.userName = request.getUserName();
        this.userBirth = request.getUserBirth();
    }

    public void updateUser(SignUpRequest request) {
        this.userBirth = request.getUserBirth();
        this.userName = request.getUserName();
        this.userPw = request.getUserPw();
        this.userId = request.getUserId();
    }

    public void addChatRoom(List<ChatRoom> chatRoom) {
        this.participantChatRooms = chatRoom;
    }
}


