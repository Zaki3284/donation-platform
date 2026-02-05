package com.donationplatform.config;

import com.donationplatform.entity.Role;
import com.donationplatform.entity.User;
import com.donationplatform.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void init() {
        // تحقق إذا كان المستخدم موجود مسبقًا
        if (userRepository.findByEmail("admin@example.com").isEmpty()) {
            User admin = new User();
            admin.setNom("Admin");
            admin.setEmail("admin@example.com");
            admin.setPassword(passwordEncoder.encode("admin123")); // كلمة المرور
            admin.setRole(Role.ADMIN);
            admin.setEnabled(true);
            admin.setAccountNonLocked(true);

            userRepository.save(admin);
            System.out.println("✅ Admin user created successfully!");
        }
    }
}
