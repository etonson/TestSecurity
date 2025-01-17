package com.example.testsecurity.services;

import com.example.testsecurity.entities.AppUserEntity;
import com.example.testsecurity.entities.AppUserRole;
import com.example.testsecurity.entities.AuthenticationResponse;
import com.example.testsecurity.entities.LoginRequest;
import com.example.testsecurity.entities.RegisterRequest;
import com.example.testsecurity.entities.StatusResponse;
import com.example.testsecurity.repositories.AppUserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.Authenticator;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/*
    @author : Eton.lin
    @description TODO
    @date 2025-01-14 上午 02:16
*/
@Service
@Slf4j
@AllArgsConstructor
public class AuthenticationService {
    private final AppUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    /*
      註冊
   */
    public StatusResponse register(RegisterRequest request) {
        var user = AppUserEntity.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .userRole(AppUserRole.USER)
                .build();
        userRepository.save(user);
        return new StatusResponse("成功");
    }

    /*
      登入
     */
    public AuthenticationResponse login(LoginRequest request) {

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
        ));
        var user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        log.info(jwtToken);
        return AuthenticationResponse.builder()
                .status("成功")
                .token(jwtToken)
                .build();
    }
}