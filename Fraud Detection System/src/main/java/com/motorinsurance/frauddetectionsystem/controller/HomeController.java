package com.motorinsurance.frauddetectionsystem.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "Motor Insurance Fraud Detection Backend is Running!";
    }
}