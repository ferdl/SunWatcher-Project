package com.ferry.sunservice.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    // Erzeugt einen sicheren Schlüssel für den HS256 Algorithmus
    private static final SecretKey KEY = Keys.hmacShaKeyFor(
            "EinGanzGeheimerSchluesselDerMindestens32ZeichenLangIst!".getBytes()
    );

    private static final long EXPIRATION_TIME = 86400000; // 1 Tag

    public String generateToken(String username) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(KEY)
                .compact();
    }

    public String validateTokenAndGetUsername(String token) {
        return Jwts.parser()
                .verifyWith(KEY) // Das ist die Methode für 0.12.x
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
}