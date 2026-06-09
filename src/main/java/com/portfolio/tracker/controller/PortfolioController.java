package com.portfolio.tracker.controller;

import com.portfolio.tracker.dto.PortfolioDTO;
import com.portfolio.tracker.entity.Portfolio;
import com.portfolio.tracker.service.PortfolioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/portfolios")
public class PortfolioController {

    private final PortfolioService portfolioService;

    public PortfolioController(PortfolioService portfolioService) {
        this.portfolioService = portfolioService;
    }

    @PostMapping
    public ResponseEntity<Portfolio> create(@Valid @RequestBody PortfolioDTO dto) {
        return ResponseEntity.ok(portfolioService.createPortfolio(dto));
    }

    @GetMapping
    public ResponseEntity<List<Portfolio>> getAll() {
        return ResponseEntity.ok(portfolioService.getMyPortfolios());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Portfolio> getById(@PathVariable Long id) {
        return ResponseEntity.ok(portfolioService.getPortfolioById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Portfolio> update(@PathVariable Long id,
                                            @Valid @RequestBody PortfolioDTO dto) {
        return ResponseEntity.ok(portfolioService.updatePortfolio(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        portfolioService.deletePortfolio(id);
        return ResponseEntity.ok("Portfolio deleted");
    }
}