package com.example.backend.service;

import com.example.backend.dto.VirementDto;
import com.example.backend.model.Compte;
import com.example.backend.model.Virement;
import com.example.backend.repositories.CompteRepository;
import com.example.backend.repositories.VirementRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VirementService {
    private final VirementRepository virementRepository;
    private final CompteRepository compteRepository;
    public VirementService(VirementRepository virementRepository,CompteRepository compteRepository) {
        this.virementRepository = virementRepository;
        this.compteRepository = compteRepository;
    }
    public List<Virement> getAllVirements() {
        return virementRepository.findAll();
    }

    @Transactional
    public void saveVirement(VirementDto virementDto) {
        // Récupérer les deux comptes (source et destination)
        Optional<Compte> compteCibleOptional = compteRepository.findById(virementDto.getCompte().getId());
        Optional<Compte> compteSourceOptional = compteRepository.findById(virementDto.getCompte_cre().getId());

        if (compteSourceOptional.isEmpty() || compteCibleOptional.isEmpty()) {
            throw new IllegalArgumentException("Les comptes source ou cible n'existent pas.");
        }

        Compte compteSource = compteSourceOptional.get();
        Compte compteCible = compteCibleOptional.get();

        // Vérifier si le compte source a suffisamment de fonds pour effectuer le virement
        if (compteSource.getSolde() < virementDto.getAmount()) {
            throw new IllegalArgumentException("Le solde du compte source est insuffisant.");
        }

        // Débiter le compte source
        compteSource.setSolde(compteSource.getSolde() - virementDto.getAmount());

        // Créditer le compte cible
        compteCible.setSolde(compteCible.getSolde() + virementDto.getAmount());

        // Sauvegarder les comptes mis à jour
        compteRepository.save(compteSource);
        compteRepository.save(compteCible);

        // Créer une nouvelle opération de virement
        Virement virement = new Virement(
                virementDto.getId(),
                virementDto.getAmount(),
                virementDto.getDescription(),
                virementDto.getDate(),
                compteCible,
                compteSource,
                virementDto.getClient_id(),
                virementDto.getEmploye_id()
        );
        // Sauvegarder l'opération de virement
        virementRepository.save(virement);
    }

    public void deleteVirement(Long id) {
        virementRepository.deleteById(id);
    }
}
