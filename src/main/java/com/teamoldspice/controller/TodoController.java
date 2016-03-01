package com.teamoldspice.controller;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableAutoConfiguration
public class TodoController {
    @RequestMapping("/")
    public String hello(){
        return "Hello, world";
    }
}
