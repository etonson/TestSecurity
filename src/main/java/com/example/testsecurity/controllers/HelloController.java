package com.example.testsecurity.controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*
    @author : Eton.lin
    @description TODO
    @date 2025-01-15 下午 11:23
*/
@AllArgsConstructor
@RestController
@RequestMapping("/test")
public class HelloController {
    @GetMapping("/hello")
    public String authenticate(){
        return "hello";
    }
}
