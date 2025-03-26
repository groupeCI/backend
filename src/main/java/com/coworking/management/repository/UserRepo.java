package com.coworking.management.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.coworking.management.entity.RoleEnum;
import com.coworking.management.entity.User;

public interface UserRepo extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    List<User> findByRole(RoleEnum role);
    boolean existsByEmail(String email);
}