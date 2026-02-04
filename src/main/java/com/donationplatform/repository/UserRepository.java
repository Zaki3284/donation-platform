package com.donationplatform.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.donationplatform.entity.User;
import com.donationplatform.entity.Role;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    // Retourne tous les utilisateurs ayant le rôle donné
    List<User> findByRole(Role role);
}
