package com.example.testsecurity.controllers;

import com.example.testsecurity.entities.AuthenticationResponse;
import com.example.testsecurity.entities.LoginRequest;
import com.example.testsecurity.entities.RegisterRequest;
import com.example.testsecurity.entities.StatusResponse;
import com.example.testsecurity.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*
    @author : Eton.lin
    @description TODO
    @date 2025-01-14 上午 02:15
*/
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<StatusResponse> register(
            @RequestBody @Validated RegisterRequest request
    ){
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(
            @RequestBody @Validated LoginRequest request
    ) {
        return ResponseEntity.ok(service.login(request));
    }
}