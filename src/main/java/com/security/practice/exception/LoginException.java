package com.security.practice.exception;

public class LoginException extends Exception{
    public LoginException(){
        super("아이디 혹은 비밀번호가 잘못되었습니다.");
    }
}
