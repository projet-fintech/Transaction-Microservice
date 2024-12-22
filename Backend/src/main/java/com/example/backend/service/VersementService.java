package com.example.backend.service;

import com.example.backend.dto.VersementDto;
import com.example.backend.model.Compte;
import com.example.backend.model.Retrait;
import com.example.backend.model.Versement;
import com.example.backend.repositories.CompteRepository;
import com.example.backend.repositories.VersementRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VersementService {

    private final VersementRepository versementRepository;
    private final CompteRepository compteRepository;
    public VersementService(VersementRepository versementRepository,CompteRepository compteRepository) {
        this.versementRepository = versementRepository;
        this.compteRepository = compteRepository;
    }

    public List<Versement> getAllVersements() {
        return versementRepository.findAll();
    }

    @Transactional
    public void saveVersement(VersementDto versementDto) {
        // Récupérer le compte
        Optional<Compte> compteOptional = compteRepository.findById(versementDto.getCompte().getId());

        if (compteOptional.isEmpty()) {
            throw new IllegalArgumentException("Le compte n'existe pas.");
        }

        Compte compte = compteOptional.get();

        // Créer un versement (créditer le compte)
        compte.setSolde(compte.getSolde() + versementDto.getAmount());

        // Sauvegarder le compte mis à jour
        compteRepository.save(compte);

        Versement versement = new Versement(
                versementDto.getId(),
                versementDto.getAmount(),
                versementDto.getDescription(),
                versementDto.getDate(),
                versementDto.getCompte(),
                versementDto.getEmploye_id()
        );
        versementRepository.save(versement);
    }

    public void deleteVersement(Long id) {
        versementRepository.deleteById(id);
    }
}
