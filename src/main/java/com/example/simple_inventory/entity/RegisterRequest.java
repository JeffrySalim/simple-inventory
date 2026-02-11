package com.example.simple_inventory.entity;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank(message = "Username wajib diisi")
    private String username;

    @NotBlank(message = "Password wajib diisi")
    private String password;

    @NotBlank(message = "Full name wajib diisi")
    private String fullName;

    @Email(message = "Email tidak valid")
    private String email;

    private String role;
}
