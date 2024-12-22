package com.example.backend.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Compte {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double solde;
    @Enumerated(EnumType.STRING)
    private CompteType type;
    private Double taux;
    private Long idClient;

    @Transient
    public Double getTaux() {
        switch (type) {
            case GOLD: return 0.02;
            case SILVER: return 0.05;
            case TITANIUM: return 0.01;
            default: return 0.0;
        }
    }

}
