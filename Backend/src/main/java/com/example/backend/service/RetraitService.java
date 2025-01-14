package com.example.backend.service;

import com.banque.events.dto.AccountDto;
import com.example.backend.config.TransactionFeeConfig;
import com.example.backend.dto.RetraitDto;
import com.example.backend.model.Retrait;
import com.example.backend.repositories.RetraitRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@Service
public class RetraitService extends KafkaService {

    private final RetraitRepository retraitRepository;
    private final TransactionFeeConfig transactionFeeConfig;
    private final WalletService walletService;

    public RetraitService(RetraitRepository retraitRepository, TransactionFeeConfig transactionFeeConfig, WalletService walletService) {
        this.retraitRepository = retraitRepository;
        this.transactionFeeConfig = transactionFeeConfig;
        this.walletService = walletService;
    }
    public List<Retrait> getAllRetraits() {
        return retraitRepository.findAll();
    }

    @Transactional
    public void saveRetrait(RetraitDto retraitDto) throws ExecutionException, InterruptedException, TimeoutException {
        // Récupérer le compte
        AccountDto compte = getAccountById(retraitDto.getCompteId());

        if (compte == null) {
            throw new IllegalArgumentException("Le compte n'existe pas.");
        }

        // Calculer les  frais en fonction du type de compte
        String accountType = String.valueOf(compte.getAccountType());
        double feePercentage = transactionFeeConfig.getFeePercentage(accountType,"retrait");
        double transactionFee = retraitDto.getAmount() * (feePercentage/100);

        // Vérifier si le compte a suffisamment de fonds pour cpuvrir le montant et les frais
        double totalDeduction = retraitDto.getAmount() + transactionFee;
        if (compte.getBalance() < totalDeduction) {
            throw new IllegalArgumentException("Solde insuffisant pour effectuer ce retrait.");
        }

        // Débiter le compte (réduire le solde)
        updateAccountBalanceRetrait(compte,totalDeduction);
        // Créditer le wallet du bank par les frais
        walletService.creditWallet(transactionFee);

        Retrait retrait = new Retrait(
                retraitDto.getId(),
                retraitDto.getAmount(),
                retraitDto.getDescription(),
                retraitDto.getDate(),
                retraitDto.getCompteId(),
                retraitDto.getEmploye_id()
        );
        retraitRepository.save(retrait);
    }

    public void deleteRetrait(Long id) {
        retraitRepository.deleteById(id);
    }
}
