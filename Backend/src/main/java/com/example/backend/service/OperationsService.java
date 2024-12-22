package com.example.backend.service;

import com.example.backend.dto.OperationDto;
import com.example.backend.model.Operations;
import com.example.backend.repositories.OperationRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service

public class OperationsService {

    private final OperationRepository operationsRepository;
    public OperationsService(OperationRepository operationsRepository) {
        this.operationsRepository = operationsRepository;
    }

    public List<Operations> getAllOperations() {
        return operationsRepository.findAll();
    }

    public Optional<Operations> getOperationById(Long id) {
        return operationsRepository.findById(id);
    }

    public void saveOperation(OperationDto operation) {
        Operations operations = Operations.builder().id(operation.getId())
                .description(operation.getDescription())
                .date(operation.getDate())
                .amount(operation.getAmount())
                .compte(operation.getCompte())
                .build();
        operationsRepository.save(operations);
    }

    public void deleteOperation(Long id) {
        operationsRepository.deleteById(id);
    }
}
