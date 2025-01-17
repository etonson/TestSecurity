package com.example.testsecurity.entities;

import lombok.Builder;
import lombok.Data;

/*
    @author : Eton.lin
    @description TODO
    @date 2025-01-15 下午 10:45
*/
@Data
@Builder
public class AuthenticationResponse {
    private String status;
    private String token;
}
