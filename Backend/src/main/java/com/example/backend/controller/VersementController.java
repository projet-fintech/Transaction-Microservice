package com.example.backend.controller;


import com.example.backend.dto.FactureDto;
import com.example.backend.dto.VersementDto;
import com.example.backend.model.Factures;
import com.example.backend.model.Versement;
import com.example.backend.service.VersementService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@AllArgsConstructor
@RestController
@RequestMapping("/api/Versement")

@CrossOrigin(origins = "*")
public class VersementController {
    private final VersementService versementService;

    @PostMapping("/add-vers")
    public void addVersement(@RequestBody VersementDto versementDto) throws ExecutionException, InterruptedException, TimeoutException {
        versementService.saveVersement(versementDto);
    }

    @GetMapping("/get-all-versements")
    public List<Versement> getAllVersements() {
        return versementService.getAllVersements();
    }



    @DeleteMapping("/{versId}")
    public void deleteVersement(@PathVariable Long versId) {
        versementService.deleteVersement(versId);
    }
}
