package com.example.backend.service;

import com.banque.events.dto.AccountDto;
import com.banque.events.dto.MonthlyLoanPaying;
import com.example.backend.model.Operations;
import com.example.backend.repositories.OperationRepository;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@Service
public class MonthlyPaymentService extends KafkaService{

    private final OperationRepository operationRepository;
    private final WalletService walletService ;


    public MonthlyPaymentService(OperationRepository operationRepository, WalletService walletService) {
        this.operationRepository = operationRepository;
        this.walletService = walletService;
    }

    public void loanPayementMonthly(MonthlyLoanPaying request) throws ExecutionException, InterruptedException, TimeoutException {
        // Récupérer le compte qui doit payer loan deduction
        AccountDto compte = getAccountById(request.getCompteId());

        if (compte == null) {
            throw new IllegalArgumentException("le compte n'existe pas!");
        }

        // Vérifier si le compte a suffisament de fonds pour effectuer le virement
        if (compte.getBalance() < request.getAmount()){
            throw new IllegalArgumentException("le solde du compte est insuffisant!");
        }

        // Débiter les frais mensuels de loan de ce compte
        compte.setBalance(compte.getBalance() - request.getAmount());
        // ajouter le montant débiter au wallet du bank
        walletService.creditWallet(request.getAmount());

        // Enregistrer dans l'archive
        Operations operations = new Operations(
               "payement de credit",
                -request.getAmount(),
                request.getCompteId(),
                "LOAN PAYMENT",
                LocalDateTime.now()
        );
        operationRepository.save(operations);

        // Envoyer une demande de mise à jour du solde de compte
        updateAccountBalanceRetrait(compte, request.getAmount());
    }
}
