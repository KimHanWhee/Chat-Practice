package com.security.practice.exception;

public class AlreadyExistException extends Exception{
    public AlreadyExistException(){
        super("이미 참여하고있는 방입니다!");
    }
}
