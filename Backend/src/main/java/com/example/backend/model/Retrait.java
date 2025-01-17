package com.example.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Retrait extends Operations {
    private Long employe_id;
    public Retrait(Long id, Double amount, String description, LocalDateTime date, UUID compteId,String type ,Long employe_id) {
        super(id, description, date,amount, compteId,type);  // Appel du constructeur parent Operations
        this.employe_id = employe_id;
    }
}
