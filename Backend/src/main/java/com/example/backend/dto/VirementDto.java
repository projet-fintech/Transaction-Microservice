package com.example.backend.dto;

import com.example.backend.model.Compte;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class VirementDto {
    private Long id;
    private String description;
    private LocalDateTime date;
    private Double amount;
    private Compte compte;
    private Long employe_id;
    private Long client_id;
    private Compte compte_cre;
}
