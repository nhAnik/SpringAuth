package com.nhanik.springauth.controller;

import com.nhanik.springauth.payload.ResetPasswordRequest;
import com.nhanik.springauth.service.ResetPasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class ResetPasswordController {

    @Autowired
    ResetPasswordService resetPasswordService;

    @PostMapping("forget_password")
    public ResponseEntity<?> validateEmailAndSendMail(@Valid @RequestBody ResetPasswordRequest request) {
        resetPasswordService.validateEmailAndSendMail(request);
        return ResponseEntity.ok("Reset link sent to mail");
    }

    @GetMapping("reset_password")
    public ResponseEntity<?> resetPassword(@RequestParam("reset_password_token") String token) {
        resetPasswordService.validateResetPasswordToken(token);
        return ResponseEntity.ok("Token validation successful");
    }
}
