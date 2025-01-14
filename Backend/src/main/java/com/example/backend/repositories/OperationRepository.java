package com.example.backend.repositories;

import com.example.backend.model.Operations;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OperationRepository extends JpaRepository<Operations, Long> {
    // Fetch operations for a list of account IDs
    List<Operations> findAllByCompteIdIn(List<UUID> compteIds);

}
