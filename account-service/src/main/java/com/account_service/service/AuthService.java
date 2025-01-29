package com.account_service.service;

import com.account_service.exception.InvalidCredentialsException;
import com.account_service.exception.NotFoundDataException;
import com.account_service.repository.AuthRepository;
import com.account_service.utils.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@Slf4j
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthRepository authRepository;

    public AuthService(PasswordEncoder passwordEncoder, JwtService jwtService, AuthRepository authRepository) {
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authRepository = authRepository;
    }

    public String login(String username, String pwd) {
        log.info("Logging user");
        var user = authRepository.findByUsername(username.toUpperCase());

        if (user == null) throw new InvalidCredentialsException("User or password is incorrect");
        var roles = Arrays.asList(user.getRoles().split(","));
        if (!passwordEncoder.matches(pwd, user.getPwd())) {
            throw new InvalidCredentialsException("User or password is incorrect");
        }
        return jwtService.generateToken(username, roles);
    }
}
