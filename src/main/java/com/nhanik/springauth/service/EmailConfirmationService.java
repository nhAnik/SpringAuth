package com.nhanik.springauth.service;

import com.nhanik.springauth.model.SecurityToken;
import com.nhanik.springauth.model.User;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailConfirmationService {

    private static final Logger logger = LoggerFactory.getLogger(EmailConfirmationService.class);

    private final EmailService emailService;
    private final SecurityTokenService securityTokenService;

    public void sendEmailConfirmationMail(User user) {
        String tokenUuid = securityTokenService.generateSecurityToken(user);
        emailService.sendMail(tokenUuid, user.getEmail(), true);
    }

    public SecurityToken validateAndGetToken(String token) {
        SecurityToken securityToken =  securityTokenService.validateAndGetToken(token);
        if (securityToken.getUser().isEnabled()) {
            logger.error("Email already confirmed!");
            throw new IllegalStateException("Security token is expired");
        }
        return securityToken;
    }
}
