package com.portfolio.tracker.controller;

import com.portfolio.tracker.dto.GainLossResponse;
import com.portfolio.tracker.dto.TransactionDTO;
import com.portfolio.tracker.entity.Transaction;
import com.portfolio.tracker.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/portfolios/{portfolioId}/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    public ResponseEntity<Transaction> log(@PathVariable Long portfolioId,
                                           @Valid @RequestBody TransactionDTO dto) {
        return ResponseEntity.ok(transactionService.logTransaction(portfolioId, dto));
    }

    @GetMapping
    public ResponseEntity<List<Transaction>> getAll(@PathVariable Long portfolioId) {
        return ResponseEntity.ok(transactionService.getTransactionsByPortfolio(portfolioId));
    }

    @GetMapping("/gain-loss")
    public ResponseEntity<List<GainLossResponse>> getGainLoss(@PathVariable Long portfolioId) {
        return ResponseEntity.ok(transactionService.getGainLoss(portfolioId));
    }
}