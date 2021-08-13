package com.nhanik.springauth.service;

import com.nhanik.springauth.model.User;
import com.nhanik.springauth.payload.ResetPasswordRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ResetPasswordService {

    private static final Logger logger = LoggerFactory.getLogger(ResetPasswordService.class);

    private final UserService userService;
    private final SecurityTokenService securityTokenService;
    private final EmailService emailService;

    public void validateEmailAndSendMail(ResetPasswordRequest request) {
        String email = request.getEmail();
        emailService.checkInvalidEmail(email);
        logger.info("Validate email " + email);
        User user = (User) userService.loadUserByUsername(email);
        if (!user.isEnabled()) {
            logger.error("Email is not confirmed yet!");
            throw new IllegalStateException("Unconfirmed email");
        }
        String tokenUuid = securityTokenService.generateSecurityToken(user);
        emailService.sendMail(tokenUuid, email, false);
    }

    public void validateResetPasswordToken(String token) {
        securityTokenService.validateAndGetToken(token);
    }
}
