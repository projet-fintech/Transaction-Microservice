package com.example.backend.controller;


import com.example.backend.dto.OperationDto;
import com.example.backend.model.Operations;
import com.example.backend.service.OperationsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@RestController
@RequestMapping("/api/Operation")
public class OperationsController {

    private final OperationsService operationsService;

    public OperationsController(OperationsService operationsService) {
        this.operationsService = operationsService;
    }

    @PostMapping("/add-oper")
    public void addOperation(@RequestBody OperationDto operationDto) {
        operationsService.saveOperation(operationDto);
    }


    @GetMapping("/get-all-operations")
    public List<Operations> getAllOperations() {
        return operationsService.getAllOperations();
    }


    //    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("ConsulterOperation/{OperatId}")
    public Optional<Operations> getOperationById(@PathVariable Long clientId) {
        return operationsService.getOperationById(clientId);
    }

    @DeleteMapping("/{operatId}")
    public void deletOperation(@PathVariable Long factId) {
        operationsService.deleteOperation(factId);
    }

    @GetMapping("/operationsClient/{id}")
    public List<Operations> getOperationByIDClient(@PathVariable UUID id){
        return operationsService.getOperationByIdClient(id);
    }

    @GetMapping("/operations/{compteId}")
    public ResponseEntity<List<Operations>> getOperationsByCompteId(@PathVariable UUID compteId) {
        List<Operations> operations = operationsService.getOperationsByCompteId(compteId);
        if (operations.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(operations);
    }



}
