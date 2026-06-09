package com.portfolio.tracker.controller;

import com.portfolio.tracker.dto.HoldingDTO;
import com.portfolio.tracker.entity.Holding;
import com.portfolio.tracker.service.HoldingService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/portfolios/{portfolioId}/holdings")
public class HoldingController {

    private final HoldingService holdingService;

    public HoldingController(HoldingService holdingService) {
        this.holdingService = holdingService;
    }

    @PostMapping
    public ResponseEntity<Holding> add(@PathVariable Long portfolioId,
                                       @Valid @RequestBody HoldingDTO dto) {
        return ResponseEntity.ok(holdingService.addHolding(portfolioId, dto));
    }

    @GetMapping
    public ResponseEntity<List<Holding>> getAll(@PathVariable Long portfolioId) {
        return ResponseEntity.ok(holdingService.getHoldingsByPortfolio(portfolioId));
    }

    @PutMapping("/{holdingId}")
    public ResponseEntity<Holding> update(@PathVariable Long portfolioId,
                                          @PathVariable Long holdingId,
                                          @Valid @RequestBody HoldingDTO dto) {
        return ResponseEntity.ok(holdingService.updateHolding(portfolioId, holdingId, dto));
    }

    @DeleteMapping("/{holdingId}")
    public ResponseEntity<String> delete(@PathVariable Long portfolioId,
                                         @PathVariable Long holdingId) {
        holdingService.deleteHolding(portfolioId, holdingId);
        return ResponseEntity.ok("Holding deleted");
    }
}