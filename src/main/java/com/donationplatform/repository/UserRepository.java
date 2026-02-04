package com.donationplatform.repository;

import com.donationplatform.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 📋 USER REPOSITORY
 *
 * What this does:
 * - Connects to the database
 * - Finds users by email
 * - Checks if email already exists
 *
 * JpaRepository gives us FREE methods:
 * - save(user)          → Save or update user
 * - findById(id)        → Find user by ID
 * - findAll()           → Get all users
 * - delete(user)        → Delete user
 * - count()             → Count total users
 *
 * We only need to ADD custom methods (findByEmail, existsByEmail)
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Find user by email
     *
     * Used when:
     * - User tries to login → find user by email
     * - Checking if email exists → before registration
     * - Loading user for authentication → Spring Security
     *
     * @param email - User's email address
     * @return Optional<User> - User if found, empty if not found
     *
     * Example:
     * Optional<User> user = userRepository.findByEmail("john@example.com");
     * if (user.isPresent()) {
     *     System.out.println("User found: " + user.get().getNom());
     * } else {
     *     System.out.println("User not found");
     * }
     */
    Optional<User> findByEmail(String email);

    /**
     * Check if email already exists
     *
     * Used when:
     * - User tries to register → check if email taken
     * - Before creating new user → validate uniqueness
     *
     * @param email - Email to check
     * @return true if email exists, false if not
     *
     * Example:
     * if (userRepository.existsByEmail("john@example.com")) {
     *     throw new Exception("Email already used!");
     * }
     */
    boolean existsByEmail(String email);

    // ✅ That's it! Spring Data JPA generates the SQL automatically!
}

/**
 * 🔍 HOW IT WORKS
 *
 * Spring Data JPA is "magic":
 * - You write method name: findByEmail
 * - Spring generates SQL: SELECT * FROM users WHERE email = ?
 *
 * Method naming rules:
 * - findBy + FieldName       → SELECT WHERE field = ?
 * - existsBy + FieldName     → SELECT COUNT WHERE field = ?
 * - deleteBy + FieldName     → DELETE WHERE field = ?
 *
 * Examples:
 * findByNom(String nom)                    → WHERE nom = ?
 * findByRole(Role role)                    → WHERE role = ?
 * findByEmailAndEnabled(String email, boolean enabled)
 *                                          → WHERE email = ? AND enabled = ?
 */

/**
 * 🎯 USAGE EXAMPLES
 *
 * In AuthService.java:
 *
 * @Service
 * public class AuthService {
 *
 *     @Autowired
 *     private UserRepository userRepository;
 *
 *     // Example 1: Login
 *     public LoginResponse login(String email, String password) {
 *         // Find user by email
 *         User user = userRepository.findByEmail(email)
 *             .orElseThrow(() -> new RuntimeException("User not found"));
 *
 *         // Check password
 *         if (!passwordEncoder.matches(password, user.getPassword())) {
 *             throw new RuntimeException("Wrong password");
 *         }
 *
 *         // Generate token...
 *     }
 *
 *     // Example 2: Register
 *     public void register(RegisterRequest request) {
 *         // Check if email exists
 *         if (userRepository.existsByEmail(request.getEmail())) {
 *             throw new RuntimeException("Email already used!");
 *         }
 *
 *         // Create new user
 *         User user = new User();
 *         user.setEmail(request.getEmail());
 *         user.setPassword(passwordEncoder.encode(request.getPassword()));
 *
 *         // Save to database
 *         userRepository.save(user);
 *     }
 * }
 */

/**
 * 🗄️ DATABASE TABLE
 *
 * This repository works with the "users" table:
 *
 * CREATE TABLE users (
 *     id BIGSERIAL PRIMARY KEY,
 *     nom VARCHAR(255),
 *     email VARCHAR(255) UNIQUE,
 *     password VARCHAR(255),
 *     telephone VARCHAR(20),
 *     role VARCHAR(20),
 *     enabled BOOLEAN,
 *     account_non_locked BOOLEAN,
 *     last_login TIMESTAMP
 * );
 *
 * Spring JPA creates this table automatically from User.java entity!
 */

/**
 * ⚠️ IMPORTANT NOTES
 *
 * 1. Optional<User> vs User:
 *    - Optional<User> = Safe (handles null)
 *    - User = Dangerous (can be null)
 *
 *    GOOD:
 *    Optional<User> user = userRepository.findByEmail(email);
 *    if (user.isPresent()) { ... }
 *
 *    BAD:
 *    User user = userRepository.findByEmail(email); // Error! Returns Optional
 *
 * 2. Email must be unique:
 *    - Add @Column(unique = true) in User.java
 *    - Or add unique constraint in database
 *
 * 3. This is an interface, not a class:
 *    - Don't write implementation
 *    - Spring generates code automatically
 */