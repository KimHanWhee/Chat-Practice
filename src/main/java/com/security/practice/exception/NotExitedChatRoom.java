package com.security.practice.exception;

public class NotExitedChatRoom extends Exception{
    public NotExitedChatRoom(){
        super("존재하지 않는 채팅방입니다.");
    }
}
