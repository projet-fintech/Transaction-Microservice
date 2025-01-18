package com.example.backend.controller;

import com.banque.events.dto.WalletUpdateRequestDto;
import com.example.backend.service.WalletService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wallet")
@CrossOrigin(origins = "*")
public class WalletController {
    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @PostMapping("/update")
    public ResponseEntity<String> updateWallet(@RequestBody WalletUpdateRequestDto request) {
        try {
            walletService.updateWallet(request.getAmount());
            return new ResponseEntity<>("Wallet updated successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error while updating the bank wallet: "+ e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
