package com.example.backend.service;

import com.banque.events.dto.AccountDto;
import com.example.backend.dto.OperationDto;
import com.example.backend.model.Operations;
import com.example.backend.repositories.OperationRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
public class OperationsService {

    private final OperationRepository operationsRepository;
    private final RestTemplate restTemplate;

    @Value("${account.service.url}") // URL of the account microservice
    private String accountServiceUrl;


    public OperationsService(OperationRepository operationsRepository, RestTemplate restTemplate) {
        this.operationsRepository = operationsRepository;
        this.restTemplate = restTemplate;
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
                .compteId(operation.getCompteId())
                .build();
        operationsRepository.save(operations);
    }

    public void deleteOperation(Long id) {
        operationsRepository.deleteById(id);
    }

    public List<Operations> getOperationByIdClient(UUID idClient) {
        // Fetch accounts from the account microservice
        String url = accountServiceUrl + "/client/" + idClient;
        ResponseEntity<AccountDto[]> response = restTemplate.getForEntity(url, AccountDto[].class);

        // Check response and handle errors
        if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null) {
            throw new RuntimeException("Failed to fetch accounts for client: " + idClient);
        }

        // Extract account IDs
        List<UUID> accountIds = Arrays.stream(response.getBody())
                .map(AccountDto::getId_account)
                .toList();

        // Fetch and return operations for all the accounts of the client
        return operationsRepository.findAllByCompteIdIn(accountIds);
    }

    public List<Operations> getOperationsByCompteId(UUID compteId) {
        return operationsRepository.findByCompteId(compteId);
    }

}
