package com.example.backend.service;


import com.example.backend.model.BankWallet;
import com.example.backend.repositories.BankWalletRepository;
import org.springframework.stereotype.Service;

@Service
public class WalletService {

    private final BankWalletRepository bankWalletRepository;


    public WalletService(BankWalletRepository bankWalletRepository) {
        this.bankWalletRepository = bankWalletRepository;
    }

    public BankWallet getBankWallet() {
        return bankWalletRepository.findById(1).orElseGet(() ->{
            BankWallet newWallet= new BankWallet(1,0.0);
            bankWalletRepository.save(newWallet);
            return newWallet;
        });
    }

    public void updateWallet(double amount) {
        BankWallet wallet = getBankWallet();
        wallet.setBalance(amount);
        bankWalletRepository.save(wallet);
    }
    public void creditWallet(double amount) {
        BankWallet wallet = getBankWallet();
        wallet.setBalance(wallet.getBalance() + amount);
        bankWalletRepository.save(wallet);
    }
}
