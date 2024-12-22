package com.example.backend.controller;


import com.example.backend.dto.FactureDto;
import com.example.backend.dto.OperationDto;
import com.example.backend.model.Factures;
import com.example.backend.model.Operations;
import com.example.backend.service.FacturesService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping("/api/Factures")
public class FacturesController {
    private final FacturesService facturesService;

    @PostMapping("/add-fact")
    public void addFacture(@RequestBody FactureDto factureDto) {
        facturesService.saveFacture(factureDto);
    }

    @GetMapping("/get-all-factures")
    public List<Factures> getAllFactures() {
        return facturesService.getAllFactures();
    }



    @DeleteMapping("/{factId}")
    public void deleteFacture(@PathVariable Long factId) {
        facturesService.deleteFacture(factId);
    }
}
