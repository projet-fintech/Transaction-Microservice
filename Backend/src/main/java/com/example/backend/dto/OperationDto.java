package com.example.backend.dto;

import java.time.LocalDateTime;
import  lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class OperationDto {
    private Long id;
    private String description;
    private LocalDateTime date;
    private Double amount;
    private UUID compteId;
}
