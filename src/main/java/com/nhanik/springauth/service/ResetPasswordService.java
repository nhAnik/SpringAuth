package com.nhanik.springauth.service;

import com.nhanik.springauth.model.User;
import com.nhanik.springauth.payload.ResetPasswordRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ResetPasswordService {

    private static final Logger logger = LoggerFactory.getLogger(ResetPasswordService.class);

    @Autowired
    private UserService userService;

    @Autowired
    private SecurityTokenService securityTokenService;

    @Autowired
    private EmailService emailService;

    public void validateEmailAndSendMail(ResetPasswordRequest request) {
        String email = request.getEmail();
        if (emailService.isInvalidEmail(email)) {
            logger.error("Malformed email");
            throw new IllegalStateException("Provided email is malformed");
        }
        logger.info("Validate email " + email);
        User user = (User) userService.loadUserByUsername(email);
        if (user.isEnabled() == false) {
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
