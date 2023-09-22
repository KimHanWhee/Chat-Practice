package com.security.practice.service;

import com.security.practice.domain.entity.Users;
import com.security.practice.domain.reponse.SignInResponse;
import com.security.practice.domain.reponse.UserResponse;
import com.security.practice.domain.request.SignInRequest;
import com.security.practice.domain.request.SignUpRequest;
import com.security.practice.exception.LoginException;
import com.security.practice.exception.TokenException;
import com.security.practice.repository.UserRepository;
import com.security.practice.security.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public ResponseEntity<String> signUp(SignUpRequest request) {
        try{
            String encodedPassword = passwordEncoder.encode(request.getUserPw());
            Users user = new Users(request, encodedPassword);
            userRepository.save(user);
            return new ResponseEntity<>("회원가입에 성공하였습니다.", HttpStatus.OK);
        }catch (Exception e) {
            return new ResponseEntity<>("이미 가입된 정보입니다.", HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<SignInResponse> signIn(SignInRequest request) throws LoginException {
        Optional<Users> logIn = userRepository.findByUserId(request.getUserId());
        if(logIn.isPresent() && passwordEncoder.matches(request.getUserPw(), logIn.get().getUserPw())){
            jwtService.createAccessToken(logIn.get().getId());
            String refreshToken = jwtService.createRefreshToken(logIn.get().getId());
            SignInResponse response = new SignInResponse(logIn.get().getUserName(), refreshToken, logIn.get().getUserId());
            return ResponseEntity.ok(response);
        } else throw new LoginException();
    }

    public ResponseEntity<UserResponse> getMe() throws TokenException {
        log.info(jwtService.getAccessToken());
        try {
            Long id = jwtService.tokenToDTO(jwtService.getAccessToken()).getId();
            Optional<Users> findById = userRepository.findById(id);
            Users users = findById.orElseThrow(TokenException::new);
            UserResponse userResponse = new UserResponse(users);
            return ResponseEntity.ok(userResponse);
        }catch(Exception e){
            throw new TokenException();
        }
    }

    public ResponseEntity<String> updateUser(SignUpRequest request) throws TokenException {
        try{
            Long id = jwtService.tokenToDTO(jwtService.getAccessToken()).getId();
            Optional<Users> findById = userRepository.findById(id);
            Users users = findById.orElseThrow(TokenException::new);
            users.updateUser(request);
            userRepository.save(users);
            return ResponseEntity.ok("수정되었습니다.");
        } catch (Exception e){
            throw new TokenException();
        }
    }
}
