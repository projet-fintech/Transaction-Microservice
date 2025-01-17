package com.example.backend.repositories;

import com.example.backend.dto.OperationResponseDto;
import com.example.backend.model.Operations;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface OperationRepository extends JpaRepository<Operations, Long> {
    // Fetch operations for a list of account IDs
    List<Operations> findAllByCompteIdIn(List<UUID> compteIds);


    List<Operations> findByCompteId(UUID compteId);

    @Query("SELECT o FROM Operations  o WHERE o.compteId = :compteId")
    List<Operations> findOperationsByCompteId(@Param("compteId") UUID compteId);

    // JPQL query to select only Operation fields
    @Query("SELECT o.id, o.description, o.date, o.amount, o.compteId, o.typeOperation FROM Operations o")
    List<Object[]> findAllOperationsOnly();
    // JPQL query to select only Operation fields and map to a DTO
    @Query("SELECT NEW com.example.backend.dto.OperationResponseDto(o.id, o.description, o.date, o.amount, o.compteId, o.typeOperation) FROM Operations o")
    List<OperationResponseDto> findAllOperationsDTO();

    // JPQL query to select Operations by compteId, mapping to DTO
    @Query("SELECT NEW com.example.backend.dto.OperationResponseDto(o.id, o.description, o.date, o.amount, o.compteId, o.typeOperation) FROM Operations o WHERE o.compteId = :compteId")
    List<OperationResponseDto> findAllOperationsByCompteIdDTO(UUID compteId);
}
