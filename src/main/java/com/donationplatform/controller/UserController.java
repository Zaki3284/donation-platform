package com.donationplatform.controller;

import com.donationplatform.dto.UserDTO;
import com.donationplatform.entity.Role;
import com.donationplatform.entity.User;
import com.donationplatform.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // List Donors (ADMIN only)
    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<UserDTO>> getAllDonors() {
        List<User> donors = userService.getUsersByRole(Role.DONATEUR);
        List<UserDTO> response = donors.stream()
                .map(user -> new UserDTO(
                        user.getId().toString(),
                        user.getNom(),
                        user.getEmail(),
                        "DONOR" // Convert DONATEUR to DONOR for frontend
                ))
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }
}