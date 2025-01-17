package com.example.testsecurity.entities;

import lombok.Data;

/*
    @author : Eton.lin
    @description TODO
    @date 2025-01-15 下午 10:45
*/
@Data
public class StatusResponse {
    String status;
    public StatusResponse(String status) {
        this.status = status;
    }
}
