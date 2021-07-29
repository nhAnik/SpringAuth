package com.nhanik.springauth.service;

import com.nhanik.springauth.model.ResetPasswordToken;
import com.nhanik.springauth.model.User;
import com.nhanik.springauth.payload.ResetPasswordRequest;
import com.nhanik.springauth.repository.ResetPasswordRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class ResetPasswordService {

    private static final Logger logger = LoggerFactory.getLogger(ResetPasswordService.class);

    @Value("${jwt.resetPasswordTokenExpirationInMs}")
    private Long resetPasswordTokenExpirationInMs;

    @Autowired
    private UserService userService;

    @Autowired
    private ResetPasswordRepository resetPasswordRepository;

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

        ResetPasswordToken resetPasswordToken = new ResetPasswordToken();
        resetPasswordToken.setUser(user);
        resetPasswordToken.setExpiryDate(Instant.now().plusMillis(resetPasswordTokenExpirationInMs));
        String tokenUuid = UUID.randomUUID().toString();
        resetPasswordToken.setToken(tokenUuid);

        resetPasswordRepository.save(resetPasswordToken);

        emailService.sendMail(tokenUuid, email);
    }

    public String validateResetPasswordToken(String token) {
        logger.info("Validating reset password token");
        ResetPasswordToken resetPasswordToken = findByToken(token);
        resetPasswordRepository.delete(resetPasswordToken);

        if (resetPasswordToken.getExpiryDate().isBefore(Instant.now())) {
            logger.error("Reset password token is expired");
            throw new IllegalStateException("Reset password token is expired");
        }

        return "Token validation successful";
    }

    private ResetPasswordToken findByToken(String token) {
        return resetPasswordRepository.findByToken(token)
                .orElseThrow(() -> new IllegalStateException("No such reset password token in database"));
    }
}
