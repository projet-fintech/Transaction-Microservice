package com.example.backend.controller;


import com.example.backend.dto.RetraitDto;
import com.example.backend.dto.VirementDto;
import com.example.backend.model.Retrait;
import com.example.backend.model.Virement;
import com.example.backend.service.RetraitService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/Retrait")
public class RetraitController {
    private final RetraitService retraitService;
    @PostMapping("/add-ret")
    public void addRetrait(@RequestBody RetraitDto retraitDto) {
        retraitService.saveRetrait(retraitDto);
    }

    @GetMapping("/get-all-retraits")
    public List<Retrait> getAllRetraits() {
        return retraitService.getAllRetraits();
    }

    @DeleteMapping("/{retId}")
    public void deletRetrait(@PathVariable Long retId) {
        retraitService.deleteRetrait(retId);
    }
}
