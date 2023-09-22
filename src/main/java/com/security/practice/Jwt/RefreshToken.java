package com.security.practice.Jwt;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash(value = "refreshToken", timeToLive = 1800)
public class RefreshToken {
    @Id
    private String refreshToken;
    private Long userId;

    public RefreshToken(final String refreshToken, final Long userId){
        this.refreshToken = refreshToken;
        this.userId = userId;
    }

    public String getRefreshToken(){return refreshToken;}

    public Long getUserId(){return userId;}
}
