package com.donationplatform.auth.services;

import com.donationplatform.auth.dto.login.LoginRequest;
import com.donationplatform.auth.dto.login.LoginResponse;
import com.donationplatform.auth.dto.register.RegisterRequest;
import com.donationplatform.auth.dto.UserDto;
import com.donationplatform.entity.Role;
import com.donationplatform.entity.User;
import com.donationplatform.repository.UserRepository;
import com.donationplatform.security.JwtUtil;
import com.donationplatform.exception.BadRequestException;
import com.donationplatform.exception.UnauthorizedException;
import com.donationplatform.exception.NotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    @Transactional
    public LoginResponse login(LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
        } catch (Exception e) {
            throw new UnauthorizedException("Email ou mot de passe incorrect");
        }

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new NotFoundException("Utilisateur introuvable"));

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);

        UserDto userDto = new UserDto(user.getId(), user.getNom(), user.getEmail(), user.getRole().name());
        return new LoginResponse(token, userDto);
    }

    @Transactional
    public void register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email déjà utilisé");
        }

        User user = new User();
        user.setNom(request.getNom());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.DONATEUR);
        user.setEnabled(true);
        user.setAccountNonLocked(true);

        userRepository.save(user);
    }

    public UserDto getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Utilisateur introuvable"));
        return new UserDto(user.getId(), user.getNom(), user.getEmail(), user.getRole().name());
    }
}
