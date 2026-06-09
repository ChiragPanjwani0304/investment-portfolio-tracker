package com.portfolio.tracker.service;

import com.portfolio.tracker.entity.StockPrice;
import com.portfolio.tracker.repository.StockPriceRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Service
public class AlphaVantageService {

    @Value("${alphavantage.api.key}")
    private String apiKey;

    @Value("${alphavantage.base.url}")
    private String baseUrl;

    private final StockPriceRepository stockPriceRepository;
    private final RestTemplate restTemplate;

    public AlphaVantageService(StockPriceRepository stockPriceRepository) {
        this.stockPriceRepository = stockPriceRepository;
        this.restTemplate = new RestTemplate();
    }

    public void refreshPrice(String symbol) {
        try {
            String url = baseUrl + "?function=GLOBAL_QUOTE&symbol=" + symbol + "&apikey=" + apiKey;

            Map<String, Object> response = restTemplate.getForObject(url, Map.class);

            if (response == null || !response.containsKey("Global Quote")) return;

            Map<String, String> quote = (Map<String, String>) response.get("Global Quote");

            if (quote == null || quote.isEmpty()) return;

            String priceStr = quote.get("05. price");
            String prevCloseStr = quote.get("08. previous close");
            String changeStr = quote.get("09. change");
            String changePctStr = quote.get("10. change percent");

            if (priceStr == null) return;

            BigDecimal currentPrice = new BigDecimal(priceStr);
            BigDecimal previousClose = prevCloseStr != null ? new BigDecimal(prevCloseStr) : null;
            BigDecimal dayChange = changeStr != null ? new BigDecimal(changeStr) : null;
            BigDecimal dayChangePct = null;

            if (changePctStr != null) {
                String cleaned = changePctStr.replace("%", "").trim();
                dayChangePct = new BigDecimal(cleaned);
            }

            StockPrice stockPrice = stockPriceRepository.findBySymbol(symbol)
                    .orElse(new StockPrice());

            stockPrice.setSymbol(symbol);
            stockPrice.setCurrentPrice(currentPrice);
            stockPrice.setPreviousClose(previousClose);
            stockPrice.setDayChange(dayChange);
            stockPrice.setDayChangePercent(dayChangePct);
            stockPrice.setLastUpdated(LocalDateTime.now());

            stockPriceRepository.save(stockPrice);

        } catch (Exception e) {
            System.err.println("Failed to refresh price for " + symbol + ": " + e.getMessage());
        }
    }

    public StockPrice getPrice(String symbol) {
        return stockPriceRepository.findBySymbol(symbol)
                .orElseThrow(() -> new RuntimeException("Price not found for: " + symbol));
    }
}