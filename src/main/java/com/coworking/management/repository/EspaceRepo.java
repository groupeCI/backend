package com.coworking.management.repository;

import com.coworking.management.entity.Espace;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EspaceRepo extends JpaRepository<Espace, Long> {
}