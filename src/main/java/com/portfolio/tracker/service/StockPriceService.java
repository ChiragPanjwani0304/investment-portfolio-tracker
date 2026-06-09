package com.portfolio.tracker.service;

import com.portfolio.tracker.entity.StockPrice;
import com.portfolio.tracker.repository.HoldingRepository;
import com.portfolio.tracker.repository.StockPriceRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StockPriceService {

    private final StockPriceRepository stockPriceRepository;
    private final HoldingRepository holdingRepository;
    private final AlphaVantageService alphaVantageService;

    public StockPriceService(StockPriceRepository stockPriceRepository,
                             HoldingRepository holdingRepository,
                             AlphaVantageService alphaVantageService) {
        this.stockPriceRepository = stockPriceRepository;
        this.holdingRepository = holdingRepository;
        this.alphaVantageService = alphaVantageService;
    }

    public List<StockPrice> getAllCachedPrices() {
        return stockPriceRepository.findAll();
    }

    public StockPrice getPriceBySymbol(String symbol) {
        return alphaVantageService.getPrice(symbol.toUpperCase());
    }

    public String manualRefresh() {
        refreshAllHoldingPrices();
        return "Price refresh triggered successfully";
    }

    // Called by the scheduler — refreshes every unique symbol across all holdings
    public void refreshAllHoldingPrices() {
        List<String> symbols = holdingRepository.findAll()
                .stream()
                .map(h -> h.getSymbol())
                .distinct()
                .collect(Collectors.toList());

        for (String symbol : symbols) {
            alphaVantageService.refreshPrice(symbol);
        }
    }
}