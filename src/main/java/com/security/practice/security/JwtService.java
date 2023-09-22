package com.security.practice.security;

import com.security.practice.Jwt.RefreshToken;
import com.security.practice.repository.JwtRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.security.Key;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtService {
    private final JwtRepository jwtRepository;

    @Value("${jwt.ACCESS_SECRET_KEY}")
    private String AccessSecret;

    @Value("${jwt.REFRESH_SECRET_KEY}")
    private String RefreshSecret;


    public void setAccessTokenHttpOnlyCookie(HttpServletResponse response, String accessToken){
        // 쿠키 생성 및 값 설정
        response.addHeader("Set-Cookie", ResponseCookie.from("accessToken", accessToken)
                .maxAge(60 * 30)
                .path("/")
//                .sameSite("None")
//                .secure(true)
//                .httpOnly(true)
                .build().toString());
    }

    public void createAccessToken(Long id){
        byte[] keyBytes = Decoders.BASE64.decode(AccessSecret);
        Key key = Keys.hmacShaKeyFor(keyBytes);
        Date now = new Date();
        String accessToken = Jwts.builder()
                .setHeaderParam("type", "jwt")
                .claim("id", id)
                .setIssuedAt(now)
                .setExpiration(new Date(System.currentTimeMillis() + (1000 * 60 * 30)))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getResponse();
        assert response != null;
        setAccessTokenHttpOnlyCookie(response, accessToken);
    }

    public String createRefreshToken(Long id){
        byte[] keyBytes = Decoders.BASE64.decode(RefreshSecret);
        Key key = Keys.hmacShaKeyFor(keyBytes);
        Date now = new Date();
        String jwtToken = Jwts.builder()
                .setHeaderParam("type", "jwt")
                .setIssuedAt(now)
                .setExpiration(new Date(System.currentTimeMillis()+ (1000 * 3600 * 24 * 7))) // 만료기간은 1주일로 설정
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
        RefreshToken refreshToken = new RefreshToken(jwtToken, id);
        jwtRepository.save(refreshToken);
        return refreshToken.getRefreshToken();
    }

    public String getAccessToken(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        Cookie[] cookies = request.getCookies();
        if(cookies != null){
            for(Cookie cookie : cookies) {

                if(cookie.getName().equals("accessToken")){
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    public String getRefreshToken(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        return request.getHeader("refreshToken");
    }

    public TokenInfo tokenToDTO(String accessToken){
        try{
            Claims claims = Jwts
                    .parserBuilder()
                    .setSigningKey(AccessSecret)
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();
            TokenInfo info = new TokenInfo().tokenToDTO(claims);
            return info;
        }catch (Exception e){
            return null;
        }
    }
}
