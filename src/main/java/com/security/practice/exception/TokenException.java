package com.security.practice.exception;

public class TokenException extends Exception{
    public TokenException(){
        super("로그아웃 처리 되었습니다");
    }
}
