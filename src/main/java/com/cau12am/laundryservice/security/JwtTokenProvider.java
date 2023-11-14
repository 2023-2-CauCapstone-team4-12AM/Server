package com.cau12am.laundryservice.security;

import com.cau12am.laundryservice.domain.UserMemberRepository;
import io.jsonwebtoken.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    @Value("${jwt.secretKey}")
    private String secretKey;

    private final UserDetailServiceImpl userDetailService;
    private final UserMemberRepository userMemberRepository;
    private static final long ACCESS_TIME = 60 * 60 * 1000L; // 1시간
    private static final long REFRESH_TIEM = 60 * 60 * 24 * 7 * 1000L; // 7일

    @PostConstruct
    public void encodeKey(){
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public String createAccessToken(Authentication authentication, String authorities){
        log.info("토큰생성");
        Date nowDate = new Date();
        Date expireTime = new Date(nowDate.getTime() + ACCESS_TIME);

        String accessToken = Jwts.builder()
                .setSubject(authentication.getName())
                .claim("auth", authorities)
                .setExpiration(expireTime)
                .setIssuedAt(nowDate)
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();

        log.info("토큰생성완료");
        return accessToken;
    }
    public String createRefreshToken(Authentication authentication, String authorities){
        Date nowDate = new Date();
        Date expireTime = new Date(nowDate.getTime() + REFRESH_TIEM);

        String refreshToken = Jwts.builder()
                .setSubject(authentication.getName())
                .claim("auth", authorities)
                .setExpiration(expireTime)
                .setIssuedAt(nowDate)
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
        return refreshToken;
    }

    public Authentication createAuthentication (String email) {
        UserDetails userDetails = userDetailService.loadUserByUsername(email);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getEmailFromToken(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateToken(String token){
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;

        } catch (MalformedJwtException e) {

        } catch (ExpiredJwtException e) {

        } catch (UnsupportedJwtException e) {

        } catch (SignatureException e) {

        }
        return false;
    }

    public ResponseEntity<Map<String, Object>> validateTokenWithInfo(String token) {
        Map<String, Object> result = new HashMap<>();

        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);

            result.put("code", 200);
            result.put("message", "Token is valid");
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (MalformedJwtException e) {
            log.info("MalformedJwtException", e);

            result.put("code", 1);
            result.put("message", "구조적인 문제가 있는 JWT");
            return new ResponseEntity<>(result, HttpStatus.UNAUTHORIZED);
        } catch (ExpiredJwtException e) {
            log.info("ExpiredJwtException", e);

            result.put("code", 2);
            result.put("message", "유효기간 만료된 JWT");
            return new ResponseEntity<>(result, HttpStatus.UNAUTHORIZED);

        } catch (UnsupportedJwtException e) {
            log.info("UnsupportedJwtException", e);

            result.put("code", 3);
            result.put("message", "지원하지 않는 형식의 JWT");
            return new ResponseEntity<>(result, HttpStatus.UNAUTHORIZED);

        } catch (SignatureException e) {
            log.info("SignatureException", e);

            result.put("code", 4);
            result.put("message", "서명 실패 JWT");
            return new ResponseEntity<>(result, HttpStatus.UNAUTHORIZED);

        }
    }
}
