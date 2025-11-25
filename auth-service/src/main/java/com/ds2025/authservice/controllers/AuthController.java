package com.ds2025.authservice.controllers;

import com.ds2025.authservice.dtos.AuthLoginDTO;
import com.ds2025.authservice.dtos.AuthRegisterDTO;
import com.ds2025.authservice.services.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody AuthRegisterDTO req) {
        authService.register(req);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody AuthLoginDTO req) {
        String token = authService.login(req);
        return ResponseEntity.ok(Map.of("token", token));
    }
}

