package com.example.backend.service;

import com.banque.events.dto.AccountDto;
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

    public RetraitService(RetraitRepository retraitRepository) {
        this.retraitRepository = retraitRepository;
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



        // Vérifier si le compte a suffisamment de fonds
        if (compte.getBalance() < retraitDto.getAmount()) {
            throw new IllegalArgumentException("Solde insuffisant pour effectuer ce retrait.");
        }

        // Débiter le compte (réduire le solde)
        updateAccountBalanceRetrait(compte,retraitDto.getAmount());

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
