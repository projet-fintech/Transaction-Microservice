package com.example.backend.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Virement extends Operations{

    @ManyToOne
    private Compte comptecred;
    private Long client_id;
    private Long employe_id;

    public Virement(Long id, Double amount, String description, LocalDateTime date, Compte compte, Compte compte_cre,Long employe_id,Long client_id) {
        super(id, description, date,amount, compte);  // Appel du constructeur parent Operations
        this.employe_id = employe_id;
        this.client_id = client_id;
        this.comptecred = comptecred;
    }
}
