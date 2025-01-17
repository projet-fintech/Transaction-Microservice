package com.example.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "operations")
public class Operations {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;
    private LocalDateTime date;
    private Double amount;
    @Column(name = "compte_id")
    private UUID compteId;
    private String typeOperation;

    public Operations(Long id, String description, LocalDateTime date, Double amount) {
        this.id = id;
        this.description = description;
        this.date = date;
        this.amount = amount;
    }

    public Operations() {

    }

    public Operations(Long id, String description, LocalDateTime date, Double amount, UUID compteId, String typeOperation) {
        this.id = id;
        this.description = description;
        this.date = date;
        this.amount = amount;
        this.compteId = compteId;
        this.typeOperation= typeOperation;
    }

    public Operations(String description, Double amount, UUID compteId, String typeOperation,LocalDateTime date) {
        this.description = description;
        this.amount = amount;
        this.compteId = compteId;
        this.typeOperation = typeOperation;
        this.date=date;
    }
}