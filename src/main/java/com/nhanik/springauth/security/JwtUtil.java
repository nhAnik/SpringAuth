package com.nhanik.springauth.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtUtil {

    private String SECRET_KEY = "secret";

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        Date currentTime = new Date(System.currentTimeMillis());
        Date expirationTime = new Date(currentTime.getTime() + 1000 * 60 * 60);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(currentTime)
                .setExpiration(expirationTime)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }
}
