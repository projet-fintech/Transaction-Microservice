package com.example.backend.config;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class TransactionFeeConfig {

    private final Map<String,Map<String, Double>> feeStructure = new HashMap<>();

    public TransactionFeeConfig() {
        // Initialiser les frais par type de compte et opération
        feeStructure.put("TITANIUM", Map.of(
                "virement", 0.5,
                "versement", 0.3,
                "retrait", 0.7,
                "facturation", 0.4
        ));
        feeStructure.put("GOLD", Map.of(
                "virement", 0.8,
                "versement", 0.5,
                "retrait", 1.0,
                "facturation", 0.6
        ));
        feeStructure.put("SILVER", Map.of(
                "virement", 1.0,
                "versement", 0.7,
                "retrait", 1.5,
                "facturation", 0.8
        ));
    }

    public double getFeePercentage(String accountType, String operationType) {
        return feeStructure.getOrDefault(accountType, new HashMap<>())
                .getOrDefault(operationType, 0.0);
    }
}


