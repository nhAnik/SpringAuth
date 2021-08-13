package com.nhanik.springauth.controller;

import com.nhanik.springauth.model.RefreshToken;
import com.nhanik.springauth.payload.AuthenticationRequest;
import com.nhanik.springauth.payload.AuthenticationResponse;
import com.nhanik.springauth.payload.RegistrationRequest;
import com.nhanik.springauth.service.RefreshTokenService;
import com.nhanik.springauth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;
    private final RefreshTokenService refreshTokenService;

    @GetMapping("hello")
    public ResponseEntity<?> getHello() {
        return ResponseEntity.ok("Hello world!");
    }

    @PostMapping("register")
    public ResponseEntity<?>  register(@Valid @RequestBody RegistrationRequest request) {
        logger.info("Got a register request for email " + request.getEmail());
        userService.createNewUser(request);
        return ResponseEntity.ok("confirmation mail sent");
    }

    @GetMapping("register/confirm")
    public ResponseEntity<?> confirmRegister(@RequestParam("confirm_token") String token) {
        userService.confirmRegister(token);
        return ResponseEntity.ok("confirmed!");
    }

    @PostMapping("authenticate")
    public ResponseEntity<?> authenticate(@Valid @RequestBody AuthenticationRequest request) {
        logger.info("Got an authentication request for email " + request.getEmail());
        String jwt = userService.authenticateUser(request);
        RefreshToken refreshToken = refreshTokenService.generateRefreshToken(request);

        AuthenticationResponse authenticationResponse =
                new AuthenticationResponse(jwt, refreshToken.getToken(), request.getEmail());
        return ResponseEntity.ok(authenticationResponse);
    }
}
