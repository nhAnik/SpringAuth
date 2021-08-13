package com.nhanik.springauth.controller;

import com.nhanik.springauth.payload.TokenRefreshRequest;
import com.nhanik.springauth.payload.TokenRefreshResponse;
import com.nhanik.springauth.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class RefreshTokenController {

    private final RefreshTokenService refreshTokenService;

    @PostMapping("refresh_token")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody TokenRefreshRequest request) {
        String jwt = refreshTokenService.refreshToken(request);
        return ResponseEntity.ok(new TokenRefreshResponse(jwt));
    }
}
