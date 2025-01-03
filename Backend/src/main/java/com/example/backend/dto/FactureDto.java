package com.example.backend.dto;


import com.example.backend.model.TypeFacture;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class FactureDto {
    private Long id;
    private String description;
    private LocalDateTime date;
    private Double amount;
    private UUID compteID;
    private TypeFacture type;
    private String reference;
    private Long clientId;

}
