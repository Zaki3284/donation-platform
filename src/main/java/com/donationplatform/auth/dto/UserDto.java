package com.donationplatform.auth.dto;

/**
 * 👤 USER DTO (Data Transfer Object)
 *
 * User information WITHOUT sensitive data
 *
 * ✅ SECURITY: Password is NEVER included!
 *
 * USED FOR:
 * - Sending user info to frontend
 * - API responses
 * - Display user details
 *
 * WHY DTO?
 * - Don't expose password
 * - Don't send unnecessary data
 * - Clean API responses
 */
public class UserDto {

    private Long id;
    private String nom;
    private String email;
    private String role;  // "ADMIN" or "DONATEUR"

    // ✅ NO PASSWORD - Never expose passwords!

    // Constructors
    public UserDto() {}

    public UserDto(Long id, String nom, String email, String role) {
        this.id = id;
        this.nom = nom;
        this.email = email;
        this.role = role;
    }

    // Getters & Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}