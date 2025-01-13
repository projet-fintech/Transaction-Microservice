package com.example.backend.controller;


import com.example.backend.dto.FactureDto;
import com.example.backend.model.Factures;
import com.example.backend.model.PredefinedBiller;
import com.example.backend.service.FacturesService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@AllArgsConstructor
@RestController
@RequestMapping("/api/Factures")
public class FacturesController {

    private final FacturesService facturesService;



    @GetMapping("/get-all-factures")
    public ResponseEntity<List<Factures>> getAllFactures() {
        List<Factures> factures =  facturesService.getAllFactures();
        return new ResponseEntity<>(factures, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Factures> getFactureById(@PathVariable Long id) {
        Factures facture = facturesService.getFactureById(id);
        if (facture != null) {
            return new ResponseEntity<>(facture, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/pay")
    public ResponseEntity<String> payFacture(@RequestBody FactureDto factureDto) {
        try {
            facturesService.payFacture(factureDto);
            return new ResponseEntity<>("Facture payement initiated successfuly.", HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }  catch (ExecutionException | InterruptedException | TimeoutException e) {
            return new ResponseEntity<>("Error while paying the facture", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/billers")
    public ResponseEntity<List<PredefinedBiller>> getAllBillers(){
        List<PredefinedBiller> billers = facturesService.getAllPredefinedBillers();
        return new ResponseEntity<>(billers, HttpStatus.OK);
    }

    @GetMapping("/billers/{id}")
    public ResponseEntity<PredefinedBiller> getBillerById(@PathVariable Integer id){
        PredefinedBiller biller = facturesService.getPredefinedBillerById(id);
        if (biller != null) {
            return new ResponseEntity<>(biller, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{factId}")
    public ResponseEntity<String> deleteFacture(@PathVariable Long factId) {
        facturesService.deleteFacture(factId);
        return new ResponseEntity<>("Facture deleted successfuly", HttpStatus.OK);
    }
}
