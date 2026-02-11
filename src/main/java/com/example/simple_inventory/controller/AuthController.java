package com.example.simple_inventory.controller;

import com.example.simple_inventory.entity.ApiResponse;
import com.example.simple_inventory.entity.RegisterRequest;
import com.example.simple_inventory.entity.RegisterResponse;
import com.example.simple_inventory.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    // Endpoint register untuk USER
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<RegisterResponse>> register(
            @RequestBody @Valid RegisterRequest request) {

        // Paksa role USER, supaya user tidak bisa bikin ADMIN
        request.setRole("USER");

        RegisterResponse response = userService.register(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>("Registrasi berhasil", response));
    }
}
