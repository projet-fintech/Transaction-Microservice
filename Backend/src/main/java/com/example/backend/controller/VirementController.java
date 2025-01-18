package com.example.backend.controller;

import com.example.backend.dto.VersementDto;
import com.example.backend.dto.VirementDto;
import com.example.backend.model.Versement;
import com.example.backend.model.Virement;
import com.example.backend.service.VirementService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@AllArgsConstructor
@RestController
@RequestMapping("/api/Virement")
@CrossOrigin(origins = "*")
public class VirementController {
    private final VirementService virementService;

    @PostMapping("/add-vire")
    public ResponseEntity<String> addVirement(@RequestBody VirementDto virementDto) throws ExecutionException, InterruptedException, TimeoutException {
        virementService.saveVirement(virementDto);
        return new ResponseEntity<>("Virement created successfully", HttpStatus.CREATED);
    }

    @GetMapping("/get-all-virements")
    public ResponseEntity<List<Virement>> getAllVirements() {
        return new ResponseEntity<>(virementService.getAllVirements(),HttpStatus.OK);
    }

    @DeleteMapping("/{virId}")
    public ResponseEntity<String> deleteVirement(@PathVariable Long virId) {
        virementService.deleteVirement(virId);
        return new ResponseEntity<>("Virement deleted sucessfuly",HttpStatus.OK);
    }
}
