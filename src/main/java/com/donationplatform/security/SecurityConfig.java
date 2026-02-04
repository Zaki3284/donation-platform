package com.donationplatform.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * 🔐 SECURITY CONFIGURATION
 *
 * This is the MAIN security setup for the app
 *
 * WHAT IT DOES:
 * 1. Decides which URLs need authentication
 * 2. Sets up JWT token checking
 * 3. Configures password encryption
 * 4. Disables sessions (we use JWT instead)
 *
 * RULES:
 * - /auth/** → Anyone can access (login, register)
 * - /api/admin/** → Only ADMIN role
 * - /api/** → Must be logged in
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity  // ✅ Allows @PreAuthorize in controllers
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final UserDetailsService userDetailsService;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter, UserDetailsService userDetailsService) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.userDetailsService = userDetailsService;
    }

    /**
     * 🛡️ SECURITY RULES
     *
     * Defines which URLs need authentication
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // ✅ Disable CSRF (not needed for REST API)
                .csrf(csrf -> csrf.disable())

                // ✅ Set authorization rules
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints (anyone can access)
                        .requestMatchers("/auth/**").permitAll()

                        // Admin-only endpoints
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")

                        // All other /api/** need authentication
                        .requestMatchers("/api/**").authenticated()

                        // Everything else requires authentication
                        .anyRequest().authenticated()
                )

                // ✅ No sessions (use JWT instead)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // ✅ Add JWT filter
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * 🔒 PASSWORD ENCODER
     *
     * BCrypt - Industry standard password encryption
     *
     * WHY BCRYPT?
     * - Slow (prevents brute force)
     * - Has built-in salt
     * - Can't be reversed
     *
     * EXAMPLE:
     * Plain: "password123"
     * BCrypt: "$2a$10$N9qo8uLOickgx2ZMRZoMye..."
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 🔑 AUTHENTICATION PROVIDER
     *
     * Tells Spring Security how to authenticate
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * 🎯 AUTHENTICATION MANAGER
     *
     * Used in AuthService for login
     */
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config
    ) throws Exception {
        return config.getAuthenticationManager();
    }
}