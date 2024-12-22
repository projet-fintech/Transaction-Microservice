package com.example.backend.dto;


import com.example.backend.model.Compte;
import com.example.backend.model.TypeFacture;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class FactureDto {
    private Long id;
    private String description;
    private LocalDateTime date;
    private Double amount;
    private Compte compte;
    private TypeFacture type;
    private String reference;
    private Long clientId;

}
