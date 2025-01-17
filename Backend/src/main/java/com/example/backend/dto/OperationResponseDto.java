package com.example.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
public class OperationResponseDto {
    private Long id;
    private String description;
    private LocalDateTime date;
    private Double amount;
    private UUID compteId;
    private String typeOperation;

}
