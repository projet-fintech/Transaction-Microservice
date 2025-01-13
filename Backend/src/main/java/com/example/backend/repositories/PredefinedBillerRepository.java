package com.example.backend.repositories;

import com.example.backend.model.PredefinedBiller;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PredefinedBillerRepository extends JpaRepository<PredefinedBiller, Integer> {
}
