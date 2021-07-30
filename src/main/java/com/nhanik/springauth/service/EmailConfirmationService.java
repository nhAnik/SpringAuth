package com.nhanik.springauth.service;

import com.nhanik.springauth.model.SecurityToken;
import com.nhanik.springauth.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmailConfirmationService {

    private static final Logger logger = LoggerFactory.getLogger(EmailConfirmationService.class);

    @Autowired
    private EmailService emailService;

    @Autowired
    private SecurityTokenService securityTokenService;

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
