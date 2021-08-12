package com.nhanik.springauth.service;

import com.nhanik.springauth.exception.ResourceNotFoundException;
import com.nhanik.springauth.model.SecurityToken;
import com.nhanik.springauth.model.User;
import com.nhanik.springauth.repository.SecurityTokenRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class SecurityTokenService {

    private static final Logger logger = LoggerFactory.getLogger(SecurityTokenService.class);

    @Value("${jwt.securityTokenExpirationInMs}")
    private Long securityTokenExpirationInMs;

    @Autowired
    private SecurityTokenRepository securityTokenRepository;

    public String generateSecurityToken(User user) {
        SecurityToken securityToken = new SecurityToken();
        securityToken.setUser(user);
        securityToken.setExpiryDate(Instant.now().plusMillis(securityTokenExpirationInMs));
        String tokenUuid = UUID.randomUUID().toString();
        securityToken.setToken(tokenUuid);

        securityTokenRepository.save(securityToken);
        return tokenUuid;
    }

    public SecurityToken validateAndGetToken(String token) {
        logger.info("Validating security token");
        SecurityToken securityToken = findByToken(token);

        if (securityToken.getExpiryDate().isBefore(Instant.now())) {
            logger.error("Security token is expired");
            throw new IllegalStateException("Security token is expired");
        }
        return securityToken;
    }

    public void removeToken(SecurityToken securityToken) {
        securityTokenRepository.delete(securityToken);
    }

    private SecurityToken findByToken(String token) {
        return securityTokenRepository.findByToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("Token"));
    }

    public SecurityToken findByUser(User user) {
        return securityTokenRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("User"));
    }
}
