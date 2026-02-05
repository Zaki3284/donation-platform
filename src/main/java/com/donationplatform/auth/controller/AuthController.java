package com.donationplatform.auth.controller;

import com.donationplatform.auth.dto.login.LoginRequest;
import com.donationplatform.auth.dto.login.LoginResponse;
import com.donationplatform.auth.dto.register.RegisterRequest;
import com.donationplatform.dto.*;
import com.donationplatform.entity.Role;
import com.donationplatform.entity.User;
import com.donationplatform.security.JwtTokenProvider;
import com.donationplatform.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthController(AuthenticationManager authenticationManager,
                          UserService userService,
                          JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            String token = jwtTokenProvider.generateToken(authentication);

            User user = userService.getUserByEmail(request.getEmail())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            LoginResponse response = new LoginResponse();
            response.setUser(new UserDTO(
                    user.getId().toString(),
                    user.getNom(),
                    user.getEmail(),
                    user.getRole().name().equals("DONATEUR") ? "DONOR" : user.getRole().name()
            ));
            response.setToken(token);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(401)
                    .body(new ErrorResponse("Invalid email or password"));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            if (userService.existsByEmail(request.getEmail())) {
                return ResponseEntity.badRequest()
                        .body(new ErrorResponse("Email already exists"));
            }

            User user = new User();
            user.setNom(request.getName());
            user.setEmail(request.getEmail());
            user.setPassword(request.getPassword());
            user.setRole(Role.DONATEUR);

            User savedUser = userService.createUser(user);

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
            String token = jwtTokenProvider.generateToken(authentication);

            LoginResponse response = new LoginResponse();
            response.setUser(new UserDTO(
                    savedUser.getId().toString(),
                    savedUser.getNom(),
                    savedUser.getEmail(),
                    "DONOR"
            ));
            response.setToken(token);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResponse("Registration failed: " + e.getMessage()));
        }
    }
}