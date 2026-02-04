package com.donationplatform.auth.dto.login;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * 📥 LOGIN REQUEST
 *
 * Data sent when user tries to login
 *
 * CONTAINS:
 * - email: User email
 * - password: User password (plain text, will be checked against encrypted)
 *
 * VALIDATION:
 * - Email must be valid format
 * - Password must not be empty
 */
public class LoginRequest {

    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "Format d'email invalide")
    private String email;

    @NotBlank(message = "Le mot de passe est obligatoire")
    private String password;

    // Getters & Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}