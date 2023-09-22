package com.security.practice.domain.reponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SignInResponse {
    private String userName;
    private String refreshToken;
    private String userId;
}
