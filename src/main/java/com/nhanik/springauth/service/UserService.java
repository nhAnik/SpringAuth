package com.nhanik.springauth.service;

import com.nhanik.springauth.model.User;
import com.nhanik.springauth.payload.AuthenticationRequest;
import com.nhanik.springauth.payload.RegistrationRequest;
import com.nhanik.springauth.repository.UserRepository;
import com.nhanik.springauth.security.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository
                .findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("User does not exist"));
    }

    public void createNewUser(RegistrationRequest request) {
        String email = request.getEmail();
        String password = request.getPassword();

        // (todo) validation of email and password

        boolean userExists = userRepository.findByEmail(email).isPresent();
        if (userExists) {
            logger.info("User with email " + email + " already exists");
            throw new IllegalStateException("User already exists");
        }

        String encodedPass = passwordEncoder.encode(password);

        logger.info("Saving new user with email " + email + " in database");
        userRepository.save(new User(email, encodedPass));
    }

    public String authenticateUser(AuthenticationRequest request) {
        String email = request.getEmail();
        String password = request.getPassword();

        // (todo) validation of email and password

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );
        } catch (BadCredentialsException e) {
            logger.info("Authentication with email " + email + " failed!");
            throw new IllegalStateException("Incorrect username or password");
        }
        logger.info("Authenticated user with email " + email);
        UserDetails userDetails = loadUserByUsername(email);
        return jwtUtil.generateToken(userDetails);
    }
}
