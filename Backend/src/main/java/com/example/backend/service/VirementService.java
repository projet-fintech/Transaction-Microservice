
package com.example.backend.service;

import com.banque.events.dto.AccountDto;
import com.example.backend.config.TransactionFeeConfig;
import com.example.backend.dto.VirementDto;
import com.example.backend.model.Operations;
import com.example.backend.model.Virement;
import com.example.backend.repositories.OperationRepository;
import com.example.backend.repositories.VirementRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;


@Service
public class VirementService extends KafkaService{

    private final VirementRepository virementRepository;
    private final TransactionFeeConfig transactionFeeConfig;
    private final OperationRepository operationRepository;
    private final WalletService walletService;

    public VirementService(VirementRepository virementRepository, TransactionFeeConfig transactionFeeConfig, OperationRepository operationRepository, WalletService walletService) {
        this.virementRepository = virementRepository;
        this.transactionFeeConfig = transactionFeeConfig;
        this.operationRepository = operationRepository;
        this.walletService = walletService;
    }
    public List<Virement> getAllVirements() {
        return virementRepository.findAll();
    }

    public void saveVirement(VirementDto virementDto) throws ExecutionException, InterruptedException, TimeoutException {
        // Récupérer les deux comptes (source et destination)
        AccountDto compteCible = getAccountById(virementDto.getCompteCreID());
        AccountDto compteSource = getAccountById(virementDto.getCompteId());

        if (compteSource == null|| compteCible == null) {
            throw new IllegalArgumentException("Les comptes source ou cible n'existent pas.");
        }

        // Vérifier si le compte source a suffisamment de fonds pour effectuer le virement
        if (compteSource.getBalance() < virementDto.getAmount()) {
            throw new IllegalArgumentException("Le solde du compte source est insuffisant.");
        }

        // Calculer les frais basé sur le type de compte et l'opération
        String accountType = String.valueOf(compteSource.getAccountType());
        System.out.println(accountType);
        double feePercentage = transactionFeeConfig.getFeePercentage(accountType,"virement");
        System.out.println(feePercentage);
        double transactionFee = virementDto.getAmount() * (feePercentage/100);
        System.out.println(transactionFee);

        // Vérifier si le compte source peut couvrir les frais
        double totalDeduction = virementDto.getAmount() + transactionFee;
        if (compteSource.getBalance() < totalDeduction) {
            throw new IllegalArgumentException("Fonds insuffisants pour couvrir les frais et le montant du virement!");
        }

        // Débiter le montant + frais du compte source et créditer le monatnt au compte cible
        compteSource.setBalance(compteSource.getBalance() - totalDeduction);
        compteCible.setBalance(compteCible.getBalance() + virementDto.getAmount());

        // Créer une nouvelle opération de virement
        Virement virement = new Virement(
                virementDto.getId(),
                -virementDto.getAmount(),
                virementDto.getDescription(),
                virementDto.getDate(),
                compteSource.getId_account(),
                "VIRMENT",
                compteCible.getId_account(),
                compteSource.getId_account(),
                virementDto.getClient_id(),
                virementDto.getEmploye_id()
        );
        Operations operations = new Operations(
                virementDto.getId()+1,
                virementDto.getDescription(),
                virementDto.getDate(),
                virementDto.getAmount(),
                virementDto.getCompteCreID(),
                "VIRMENT"
        );
        operationRepository.save(operations);
        // Sauvegarder l'opération de virement
        virementRepository.save(virement);
        // Envoyer une demande de mise à jour du solde
        updateAccountBalance(compteSource,compteCible);
        // crediter le wallet du bank par les frais
        walletService.creditWallet(transactionFee);
    }

    public void deleteVirement(Long id) {
        virementRepository.deleteById(id);
    }


}