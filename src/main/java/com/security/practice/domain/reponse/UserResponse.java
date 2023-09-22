package com.security.practice.domain.reponse;

import com.security.practice.domain.entity.Users;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserResponse {
    private String userId;
    private String userName;
    private String userBirth;

    public UserResponse(Users users) {
        this.userId = users.getUserId();
        this.userName = users.getUserName();
        this.userBirth = users.getUserBirth();
    }
}
