package com.example.backend.controller;


import com.example.backend.dto.OperationDto;
import com.example.backend.model.Operations;
import com.example.backend.service.OperationsService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@RestController
@RequestMapping("/api/Operation")
public class OperationsController {
    private final OperationsService operationsService;

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


}
