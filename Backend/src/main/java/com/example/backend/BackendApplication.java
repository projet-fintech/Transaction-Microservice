package com.example.backend;

import com.example.backend.model.Compte;
import com.example.backend.model.CompteType;
import com.example.backend.model.TypeFacture;
import com.example.backend.repositories.CompteRepository;
import com.example.backend.repositories.TypeFactureRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class BackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}
	@Bean
	public CommandLineRunner initData(CompteRepository compteRepository, TypeFactureRepository typeFactureRepository) {
		return args -> {
			// Initialisation des types de factures
			TypeFacture orange = new TypeFacture(null, "Orange", "0693057266");
			TypeFacture ryanair = new TypeFacture(null, "Ryanair", "I1258967432");
			TypeFacture amendis = new TypeFacture(null, "Amendis", "NDHK964433");
			typeFactureRepository.saveAll(List.of(orange, ryanair, amendis));

			// Création des comptes avec un taux basé sur le type
			Compte compte1 = new Compte(null, 1000.0, CompteType.GOLD, calculateTaux(CompteType.GOLD), 1L);
			Compte compte2 = new Compte(null, 500.0, CompteType.SILVER, calculateTaux(CompteType.SILVER), 2L);
			compteRepository.saveAll(List.of(compte1, compte2));

		};
	}

	// Méthode pour calculer le taux en fonction du type de compte
	private Double calculateTaux(CompteType type) {
		switch (type) {
			case GOLD:
				return 0.02;
			case SILVER:
				return 0.05;
			case TITANIUM:
				return 0.01;
			default:
				throw new IllegalArgumentException("Type de compte invalide : " + type);
		}
	}
}


