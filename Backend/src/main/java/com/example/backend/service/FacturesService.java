//package com.example.backend.service;
//
//
//import com.example.backend.dto.FactureDto;
//import com.example.backend.model.Factures;
//import com.example.backend.model.Stat;
//import com.example.backend.repositories.FacturesRepository;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//
//public class FacturesService {
//    private final FacturesRepository facturesRepository;
//
//    public FacturesService(FacturesRepository facturesRepository ) {
//        this.facturesRepository = facturesRepository;
//
//    }
//
//    public List<Factures> getAllFactures() {
//        return facturesRepository.findAll();
//    }
//
//    public void saveFacture(FactureDto factureDto) {
//
//        Compte compte=factureDto.getCompte();
//        // Vérification du solde du compte débité
//        if (compte.getSolde() < factureDto.getAmount()) {
//            throw new IllegalArgumentException("Solde insuffisant pour effectuer cette facture.");
//        }
//        compte.setSolde(compte.getSolde()-factureDto.getAmount());
//        compteRepository.save(compte);
//        // Sauvegarder la facture
//
//        // Créer une instance de Factures
//        Factures factures = new Factures(
//                factureDto.getId(),
//                factureDto.getAmount(),
//                factureDto.getDescription(),
//                factureDto.getDate(),
//                factureDto.getCompte(),
//                factureDto.getType(),  // type spécifique à Factures
//                factureDto.getReference(),
//                factureDto.getClientId(),
//                Stat.PAYE
//        );
//        facturesRepository.save(factures);
//    }
//
//    public void deleteFacture(Long id) {
//        facturesRepository.deleteById(Math.toIntExact(id));
//    }
//}
