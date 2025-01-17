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

    private Integer predefinedBillerId;
    @Column(nullable = false)
    private String reference;
    private Stat status;

    public Factures(Long id, Double amount, String description, LocalDateTime date, UUID compteId,String type ,Integer predefinedBillerId, String reference, Stat status) {
        super(id, description, date,amount, compteId,type);  // Appel du constructeur parent Operations
        this.predefinedBillerId = predefinedBillerId;
        this.reference = reference;
        this.status = status;
    }
}
