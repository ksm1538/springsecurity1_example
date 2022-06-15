package com.cos.security1.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestControllerAPI {
    @GetMapping("home")
    public String Home(){
        return "<h1>home</h1>";
    }

    @PostMapping("token")
    public String Token(){
        return "<h1>token</h1>";
    }
}
