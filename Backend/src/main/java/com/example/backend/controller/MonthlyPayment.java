package com.example.backend.controller;

import com.banque.events.dto.MonthlyLoanPaying;
import com.example.backend.service.MonthlyPaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@RestController
@RequestMapping("/payment")
public class MonthlyPayment {

    private final MonthlyPaymentService service;

    public MonthlyPayment(MonthlyPaymentService service) {
        this.service = service;
    }

    @PostMapping("/loan")
    public ResponseEntity<String> payloanMonthly(@RequestBody MonthlyLoanPaying request) throws ExecutionException, InterruptedException, TimeoutException {
        service.loanPayementMonthly(request);
        return ResponseEntity.ok("payment passed successfully");
    }
}
