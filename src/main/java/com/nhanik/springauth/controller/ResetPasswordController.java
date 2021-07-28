package com.nhanik.springauth.controller;

import com.nhanik.springauth.payload.ResetPasswordRequest;
import com.nhanik.springauth.service.ResetPasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ResetPasswordController {

    @Autowired
    ResetPasswordService resetPasswordService;

    @PostMapping("forget_password")
    public ResponseEntity<?> validateEmailAndSendMail(@RequestBody ResetPasswordRequest request) {
        resetPasswordService.validateEmailAndSendMail(request);
        return ResponseEntity.ok("Reset link sent to mail");
    }

    @GetMapping("reset_password")
    public ResponseEntity<?> resetPassword(@RequestParam("reset_password_token") String token) {
        String message = resetPasswordService.validateResetPasswordToken(token);
        return ResponseEntity.ok(message);
    }
}
