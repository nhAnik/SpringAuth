package com.nhanik.springauth.controller;

import com.nhanik.springauth.payload.ResetPasswordRequest;
import com.nhanik.springauth.service.ResetPasswordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class ResetPasswordController {

    private final ResetPasswordService resetPasswordService;

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
