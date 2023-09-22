package com.security.practice.controller;

import com.security.practice.domain.reponse.SignInResponse;
import com.security.practice.domain.reponse.UserResponse;
import com.security.practice.domain.request.SignInRequest;
import com.security.practice.domain.request.SignUpRequest;
import com.security.practice.exception.LoginException;
import com.security.practice.exception.TokenException;
import com.security.practice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Log4j2
public class UserController {
    private final UserService userService;

    @PostMapping("/sign-up")
    public ResponseEntity<String> signUp(@RequestBody SignUpRequest request){
        return userService.signUp(request);
    }

    @PostMapping("/sign-in")
    public ResponseEntity<SignInResponse> signIn(@RequestBody SignInRequest request) throws LoginException {
        return userService.signIn(request);
    }
    @GetMapping
    public ResponseEntity<UserResponse> getMe() throws TokenException {
        return userService.getMe();
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateUser(@RequestBody SignUpRequest request) throws TokenException {
        return userService.updateUser(request);
    }
}
