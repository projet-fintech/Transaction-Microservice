package com.example.backend.repositories;

import com.example.backend.model.BankWallet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankWalletRepository extends JpaRepository<BankWallet, Integer> {
}
