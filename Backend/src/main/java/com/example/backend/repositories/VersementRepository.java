package com.example.backend.repositories;

import com.example.backend.model.Versement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VersementRepository extends JpaRepository<Versement, Long> {
}
