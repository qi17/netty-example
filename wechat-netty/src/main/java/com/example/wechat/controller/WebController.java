package com.example.wechat.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    @GetMapping("/")
    public String home() {
        return "forward:/index.jsp";
    }

    @GetMapping("/index")
    public String index() {
        return "index";
    }
}