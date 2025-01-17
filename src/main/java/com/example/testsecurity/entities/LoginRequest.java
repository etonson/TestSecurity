package com.example.testsecurity.entities;

import lombok.Builder;
import lombok.Data;

/*
    @author : Eton.lin
    @description TODO
    @date 2025-01-15 下午 10:40
*/
@Data
@Builder
public class LoginRequest {
    String email;
    String password;
}
