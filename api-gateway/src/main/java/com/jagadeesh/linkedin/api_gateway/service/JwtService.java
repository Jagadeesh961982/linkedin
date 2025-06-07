package com.jagadeesh.linkedin.api_gateway.service;

import java.nio.charset.StandardCharsets;

import javax.crypto.SecretKey;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    @Value("${jwt.secretKey}")
    private String secretKey;

    private SecretKey getSecretKey(){ return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));}


    public String getUserIdFromToken(String token){
        Claims claims= Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.getSubject();
    }
    

}
