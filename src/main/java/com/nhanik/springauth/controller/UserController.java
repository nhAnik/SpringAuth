package com.nhanik.springauth.controller;

import com.nhanik.springauth.payload.AuthenticationRequest;
import com.nhanik.springauth.payload.RegistrationRequest;
import com.nhanik.springauth.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    UserService userService;

    @GetMapping("hello")
    public ResponseEntity<?> getHello() {
        return ResponseEntity.ok("Hello world!");
    }

    @PostMapping("register")
    public void register(@RequestBody RegistrationRequest request) {
        logger.info("Got a register request for email " + request.getEmail());
        userService.createNewUser(request);
    }

    @PostMapping("authenticate")
    public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequest request) {
        logger.info("Got an authentication request for email " + request.getEmail());
        String jwt = userService.authenticateUser(request);
        return ResponseEntity.ok(jwt);
    }
}
