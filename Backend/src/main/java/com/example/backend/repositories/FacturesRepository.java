package com.example.backend.repositories;

import com.example.backend.model.Factures;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FacturesRepository extends JpaRepository<Factures, Integer> {
}
