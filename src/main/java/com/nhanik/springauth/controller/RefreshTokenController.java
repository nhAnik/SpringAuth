package com.nhanik.springauth.controller;

import com.nhanik.springauth.payload.TokenRefreshRequest;
import com.nhanik.springauth.payload.TokenRefreshResponse;
import com.nhanik.springauth.service.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RefreshTokenController {

    @Autowired
    private RefreshTokenService refreshTokenService;

    @PostMapping("refresh_token")
    public ResponseEntity<?> refreshToken(@RequestBody TokenRefreshRequest request) {
        String jwt = refreshTokenService.refreshToken(request);
        return ResponseEntity.ok(new TokenRefreshResponse(jwt));
    }
}
