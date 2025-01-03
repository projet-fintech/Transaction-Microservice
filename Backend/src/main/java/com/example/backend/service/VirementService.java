package com.example.backend.service;

import com.banque.events.dto.AccountDto;
import com.example.backend.dto.VirementDto;
import com.example.backend.model.Virement;
import com.example.backend.repositories.VirementRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;


@Service
public class VirementService extends KafkaService{

    private final VirementRepository virementRepository;

    public VirementService(VirementRepository virementRepository) {
        this.virementRepository = virementRepository;
    }
    public List<Virement> getAllVirements() {
        return virementRepository.findAll();
    }

    public void saveVirement(VirementDto virementDto) throws ExecutionException, InterruptedException, TimeoutException {
        // Récupérer les deux comptes (source et destination)
        AccountDto compteCible = getAccountById(virementDto.getCompteId());
        AccountDto compteSource = getAccountById(virementDto.getCompteCreID());

        if (compteSource == null|| compteCible == null) {
            throw new IllegalArgumentException("Les comptes source ou cible n'existent pas.");
        }

        // Vérifier si le compte source a suffisamment de fonds pour effectuer le virement
        if (compteSource.getBalance() < virementDto.getAmount()) {
            throw new IllegalArgumentException("Le solde du compte source est insuffisant.");
        }

        // Créer une nouvelle opération de virement
        Virement virement = new Virement(
                virementDto.getId(),
                virementDto.getAmount(),
                virementDto.getDescription(),
                virementDto.getDate(),
                compteCible.getId_account(),
                compteSource.getId_account(),
                virementDto.getClient_id(),
                virementDto.getEmploye_id()
        );
        // Sauvegarder l'opération de virement
        virementRepository.save(virement);
        // Envoyer une demande de mise à jour du solde
        updateAccountBalance(compteSource,compteCible,virementDto.getAmount());
    }

    public void deleteVirement(Long id) {
        virementRepository.deleteById(id);
    }


}