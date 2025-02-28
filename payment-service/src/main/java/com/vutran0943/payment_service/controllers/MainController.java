package com.vutran0943.payment_service.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/cancel")
    public String cancel() {
        return "paypal/cancel";
    }

    @GetMapping("/success")
    public String success() {
        return "paypal/success";
    }

    @GetMapping("/error")
    public String error() {
        return "paypal/error";
    }
}
