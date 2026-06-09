package com.portfolio.tracker.controller;

import com.portfolio.tracker.service.StockPriceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import com.portfolio.tracker.entity.StockPrice;

@RestController
@RequestMapping("/api/prices")
public class StockPriceController {

    private final StockPriceService stockPriceService;

    public StockPriceController(StockPriceService stockPriceService) {
        this.stockPriceService = stockPriceService;
    }

    @PostMapping("/refresh")
    public ResponseEntity<String> manualRefresh() {
        return ResponseEntity.ok(stockPriceService.manualRefresh());
    }

    @GetMapping
    public ResponseEntity<List<StockPrice>> getAllPrices() {
        return ResponseEntity.ok(stockPriceService.getAllCachedPrices());
    }
}