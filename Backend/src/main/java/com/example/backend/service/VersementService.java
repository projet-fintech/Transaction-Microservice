package com.example.backend.service;

import com.banque.events.dto.AccountDto;
import com.example.backend.config.TransactionFeeConfig;
import com.example.backend.dto.VersementDto;
import com.example.backend.model.Versement;
import com.example.backend.repositories.VersementRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@Service
public class VersementService extends KafkaService{

    private final VersementRepository versementRepository;
    private final TransactionFeeConfig transactionFeeConfig;
    private final WalletService walletService;

    public VersementService(VersementRepository versementRepository, TransactionFeeConfig transactionFeeConfig, WalletService walletService) {
        this.versementRepository = versementRepository;
        this.transactionFeeConfig = transactionFeeConfig;
        this.walletService = walletService;
    }

    public List<Versement> getAllVersements() {
        return versementRepository.findAll();
    }

    @Transactional
    public void saveVersement(VersementDto versementDto) throws ExecutionException, InterruptedException, TimeoutException {
        // Récupérer le compte
        AccountDto compte = getAccountById(versementDto.getCompteId());

        if (compte == null) {
            throw new IllegalArgumentException("Le compte n'existe pas.");
        }

        // calculer les frais basés sur le type de compte et l'opération
        String accountType = String.valueOf(compte.getAccountType());
        double feePercentage = transactionFeeConfig.getFeePercentage(accountType,"versement");
        double transactionFee = versementDto.getAmount() * (feePercentage/100);
        double totalDeduction = versementDto.getAmount() + transactionFee;

        // Créer un versement (créditer le compte)
        updateAccountBalanceVersement(compte,totalDeduction);

        // Créditer le wallet du bank par les frais
        walletService.creditWallet(transactionFee);

        Versement versement = new Versement(
                versementDto.getId(),
                versementDto.getAmount(),
                versementDto.getDescription(),
                versementDto.getDate(),
                versementDto.getCompteId(),
                versementDto.getEmploye_id()
        );
        versementRepository.save(versement);
    }

    public void deleteVersement(Long id) {
        versementRepository.deleteById(id);
    }
}
