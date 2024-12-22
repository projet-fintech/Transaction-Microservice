package com.example.backend.controller;

import com.example.backend.dto.VersementDto;
import com.example.backend.dto.VirementDto;
import com.example.backend.model.Versement;
import com.example.backend.model.Virement;
import com.example.backend.service.VirementService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/Virement")
public class VirementController {
    private final VirementService virementService;

    @PostMapping("/add-vire")
    public void addVirement(@RequestBody VirementDto virementDto) {
        virementService.saveVirement(virementDto);
    }

    @GetMapping("/get-all-virements")
    public List<Virement> getAllVirements() {
        return virementService.getAllVirements();
    }

    @DeleteMapping("/{virId}")
    public void deleteVirement(@PathVariable Long virId) {
        virementService.deleteVirement(virId);
    }
}
