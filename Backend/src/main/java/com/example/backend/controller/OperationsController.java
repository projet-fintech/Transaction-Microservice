package com.example.backend.controller;


import com.example.backend.dto.OperationDto;
import com.example.backend.dto.OperationResponseDto;
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


    //    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("ConsulterOperation/{OperatId}")
    public Optional<Operations> getOperationById(@PathVariable Long clientId) {
        return operationsService.getOperationById(clientId);
    }

    @DeleteMapping("/{operatId}")
    public void deletOperation(@PathVariable Long factId) {
        operationsService.deleteOperation(factId);
    }


    // For front End

    @GetMapping("/operations/{compteId}")
    public ResponseEntity<List<OperationResponseDto>> getOperationsByCompteId(@PathVariable UUID compteId) {
        List<OperationResponseDto> operations = operationsService.findOperationsByCompteId(compteId);
        if (operations.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(operations);
    }


    @GetMapping("/operations-dto")
    public List<OperationResponseDto> getAllOperationsDTO(){
        return operationsService.getAllOperationsDTO();
    }


}
