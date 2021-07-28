package com.nhanik.springauth.service;

import com.nhanik.springauth.model.RefreshToken;
import com.nhanik.springauth.model.User;
import com.nhanik.springauth.payload.AuthenticationRequest;
import com.nhanik.springauth.payload.TokenRefreshRequest;
import com.nhanik.springauth.repository.RefreshTokenRepository;
import com.nhanik.springauth.security.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class RefreshTokenService {

    private static final Logger logger = LoggerFactory.getLogger(RefreshTokenService.class);

    @Value("${jwt.refreshTokenExpirationInMs}")
    private Long refreshTokenExpirationInMs;

    @Autowired
    private UserService userService;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private JwtUtil jwtUtil;

    public RefreshToken generateRefreshToken(AuthenticationRequest request) {
        RefreshToken refreshToken = new RefreshToken();

        // (todo) Avoid redundant database hit
        User user = (User) userService.loadUserByUsername(request.getEmail());
        refreshToken.setUser(user);
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenExpirationInMs));
        refreshToken.setToken(UUID.randomUUID().toString());

        refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    public String refreshToken(TokenRefreshRequest request) {
        logger.info("Request to refresh token");
        RefreshToken refreshToken = findByToken(request.getRefreshToken());

        if (refreshToken.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(refreshToken);
            logger.error("Refresh token is expired");
            throw new IllegalStateException("Refresh token is expired");
        }

        UserDetails userDetails = refreshToken.getUser();
        return jwtUtil.generateToken(userDetails);
    }

    private RefreshToken findByToken(String token) {
        return refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalStateException("No such refresh token in database"));
    }
}
