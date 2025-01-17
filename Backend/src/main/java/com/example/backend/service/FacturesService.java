package com.example.backend.service;


import com.banque.events.dto.AccountDto;
import com.example.backend.config.TransactionFeeConfig;
import com.example.backend.dto.FactureDto;
import com.example.backend.model.Factures;
import com.example.backend.model.PredefinedBiller;
import com.example.backend.model.Stat;
import com.example.backend.repositories.FacturesRepository;
import com.example.backend.repositories.PredefinedBillerRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@Service
public class FacturesService extends KafkaService{

    private final FacturesRepository facturesRepository;
    private final PredefinedBillerRepository predefinedBillerRepository;
    private final TransactionFeeConfig transactionFeeConfig;
    private final WalletService walletService;


    public FacturesService(FacturesRepository facturesRepository, PredefinedBillerRepository predefinedBillerRepository, TransactionFeeConfig transactionFeeConfig, WalletService walletService) {
        this.facturesRepository = facturesRepository;
        this.predefinedBillerRepository = predefinedBillerRepository;
        this.transactionFeeConfig = transactionFeeConfig;
        this.walletService = walletService;
    }

    public List<Factures> getAllFactures() {
        return facturesRepository.findAll();
    }

    public Factures getFactureById(Long id) {
        return facturesRepository.findById(id).orElse(null);
    }

    public List<PredefinedBiller> getAllPredefinedBillers() {
        return predefinedBillerRepository.findAll();
    }

    public PredefinedBiller getPredefinedBillerById(Integer id) {
        return predefinedBillerRepository.findById(id).orElse(null);
    }

    public void payFacture(FactureDto factureDto) throws ExecutionException, InterruptedException, TimeoutException {
        // recuperer le compte
        if (factureDto.getCompteID() == null) {
            throw new IllegalArgumentException("The account ID cannot be null.");
        }
        AccountDto compte = getAccountById(factureDto.getCompteID());

        if (compte == null) {
            throw new IllegalArgumentException("le compte n'existe pas.");
        }

       // calculer les frais basés sur le type de compte et l'opération
        String accountType = String.valueOf(compte.getAccountType());
        double feePercentage = transactionFeeConfig.getFeePercentage(accountType,"facturation");
        double transactionFee = factureDto.getAmount() * (feePercentage/100);
        double totalDeduction = factureDto.getAmount() + transactionFee;

        // verification si le compte a suffisamment de fonds
        if (compte.getBalance() < totalDeduction) {
            throw new IllegalArgumentException("Solde insuffisant pour effectuer ce paiement de facture.");
        }
        // debiter le compte et mettre à jour la facture
        updateAccountBalanceRetrait(compte,totalDeduction);
        // Créditer le wallet du bank
        walletService.creditWallet(transactionFee);
        PredefinedBiller biller = getPredefinedBillerById(factureDto.getBillerId());
        Factures facture = new Factures(
                factureDto.getId(),
                -factureDto.getAmount(),
                factureDto.getDescription(),
                LocalDateTime.now(),
                factureDto.getCompteID(),
                "Facture",
                biller != null ? biller.getBiller_id() : 0,
                factureDto.getReference(),
                Stat.PAYE
        );
        facturesRepository.save(facture);
    }

    public void deleteFacture(Long id) {
        facturesRepository.deleteById(id);
    }
}