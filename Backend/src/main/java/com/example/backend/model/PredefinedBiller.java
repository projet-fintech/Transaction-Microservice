package com.example.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "predefined_billers")
public class PredefinedBiller {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer biller_id;
    private String biller_name;
    private String reference_type;
    private String category;
}
