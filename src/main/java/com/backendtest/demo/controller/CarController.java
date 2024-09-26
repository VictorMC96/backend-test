package com.backendtest.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class CarController {

    @GetMapping("/test")
    public ResponseEntity<?> testController() {
        return ResponseEntity.ok("Test Controller 01");
    }
}
