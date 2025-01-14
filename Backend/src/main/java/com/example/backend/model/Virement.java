package com.example.backend.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Virement extends Operations{


    private UUID comptecred;
    private UUID compteDeb;
    private Long client_id;
    private Long employe_id;

    public Virement(Long id, Double amount, String description, LocalDateTime date, UUID comptecred,UUID compteDeb,Long employe_id,Long client_id) {
        super(id, description, date,amount);
        this.employe_id = employe_id;
        this.client_id = client_id;
        this.comptecred = comptecred;
        this.compteDeb = compteDeb;
    }
}