package com.example.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Operations {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;
    private LocalDateTime date;
    @Positive
    private Double amount;

    private UUID compteId;

    public Operations(Long id, String description, LocalDateTime date, Double amount) {
        this.id = id;
        this.description = description;
        this.date = date;
        this.amount = amount;
    }

    public Operations() {

    }

    public Operations(Long id, String description, LocalDateTime date, Double amount, UUID compteId) {
        this.id = id;
        this.description = description;
        this.date = date;
        this.amount = amount;
        this.compteId = compteId;
    }
}