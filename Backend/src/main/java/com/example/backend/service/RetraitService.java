package com.example.backend.service;

import com.example.backend.dto.RetraitDto;
import com.example.backend.dto.VirementDto;
import com.example.backend.model.Compte;
import com.example.backend.model.Factures;
import com.example.backend.model.Retrait;
import com.example.backend.repositories.CompteRepository;
import com.example.backend.repositories.RetraitRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RetraitService {
    private final RetraitRepository retraitRepository;
    private final CompteRepository compteRepository;
    public RetraitService(RetraitRepository retraitRepository, CompteRepository compteRepository) {
        this.retraitRepository = retraitRepository;
        this.compteRepository = compteRepository;
    }
    public List<Retrait> getAllRetraits() {
        return retraitRepository.findAll();
    }

    @Transactional
    public void saveRetrait(RetraitDto retraitDto) {
        // Récupérer le compte
        Optional<Compte> compteOptional = compteRepository.findById(retraitDto.getCompte().getId());

        if (compteOptional.isEmpty()) {
            throw new IllegalArgumentException("Le compte n'existe pas.");
        }

        Compte compte = compteOptional.get();

        // Vérifier si le compte a suffisamment de fonds
        if (compte.getSolde() < retraitDto.getAmount()) {
            throw new IllegalArgumentException("Solde insuffisant pour effectuer ce retrait.");
        }

        // Débiter le compte (réduire le solde)
        compte.setSolde(compte.getSolde() - retraitDto.getAmount());

        // Sauvegarder le compte mis à jour
        compteRepository.save(compte);

        Retrait retrait = new Retrait(
                retraitDto.getId(),
                retraitDto.getAmount(),
                retraitDto.getDescription(),
                retraitDto.getDate(),
                retraitDto.getCompte(),
                retraitDto.getEmploye_id()
        );
        retraitRepository.save(retrait);
    }

    public void deleteRetrait(Long id) {
        retraitRepository.deleteById(id);
    }
}
