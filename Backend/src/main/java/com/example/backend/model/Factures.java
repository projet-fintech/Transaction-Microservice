package com.example.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Factures extends Operations{

    @ManyToOne
    private TypeFacture type;
    @Column(nullable = false)
    private String reference;
    private Long clientId;
    private Stat status;
    public Factures(Long id, Double amount, String description, LocalDateTime date, UUID compteId, TypeFacture type, String reference, Long clientId, Stat status) {
        super(id, description, date,amount, compteId);  // Appel du constructeur parent Operations
        this.type = type;
        this.reference = reference;
        this.clientId = clientId;
        this.status = status;
    }
}
