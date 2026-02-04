package com.donationplatform.auth.dto.login;

import com.donationplatform.auth.dto.UserDto;

/**
 * 📤 LOGIN RESPONSE
 *
 * Data sent back after successful login
 *
 * CONTAINS:
 * - token: JWT token (user sends this with future requests)
 * - user: User information
 *
 * EXAMPLE:
 * {
 *   "token": "eyJhbGciOiJIUzUxMiJ9...",
 *   "user": {
 *     "id": 1,
 *     "nom": "John Doe",
 *     "email": "john@example.com",
 *     "role": "DONATEUR"
 *   }
 * }
 */
public class LoginResponse {

    private String token;
    private UserDto user;

    // Constructors
    public LoginResponse() {}

    public LoginResponse(String token, UserDto user) {
        this.token = token;
        this.user = user;
    }

    // Getters & Setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }
}