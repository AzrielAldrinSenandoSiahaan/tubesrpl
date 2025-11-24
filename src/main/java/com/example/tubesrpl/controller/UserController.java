package com.example.tubesrpl.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

    @GetMapping("/")
    public String showPage(){
        return "dashboard";
    }
}
