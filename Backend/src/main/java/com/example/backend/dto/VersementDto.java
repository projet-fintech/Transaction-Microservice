package com.example.backend.dto;

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
public class VersementDto {
    private Long id;
    private String description;
    private LocalDateTime date;
    private Double amount;
    private UUID compteId;
    private Long employe_id;
}
