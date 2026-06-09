package com.portfolio.tracker.service;

import com.portfolio.tracker.dto.GainLossResponse;
import com.portfolio.tracker.dto.TransactionDTO;
import com.portfolio.tracker.entity.Holding;
import com.portfolio.tracker.entity.Portfolio;
import com.portfolio.tracker.entity.StockPrice;
import com.portfolio.tracker.entity.Transaction;
import com.portfolio.tracker.repository.HoldingRepository;
import com.portfolio.tracker.repository.StockPriceRepository;
import com.portfolio.tracker.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final HoldingRepository holdingRepository;
    private final StockPriceRepository stockPriceRepository;
    private final PortfolioService portfolioService;

    public TransactionService(TransactionRepository transactionRepository,
                              HoldingRepository holdingRepository,
                              StockPriceRepository stockPriceRepository,
                              PortfolioService portfolioService) {
        this.transactionRepository = transactionRepository;
        this.holdingRepository = holdingRepository;
        this.stockPriceRepository = stockPriceRepository;
        this.portfolioService = portfolioService;
    }

    public Transaction logTransaction(Long portfolioId, TransactionDTO dto) {
        Portfolio portfolio = portfolioService.getPortfolioById(portfolioId);
        String symbol = dto.getSymbol().toUpperCase();

        Transaction tx = new Transaction();
        tx.setPortfolio(portfolio);
        tx.setSymbol(symbol);
        tx.setType(dto.getType().toUpperCase());
        tx.setQuantity(dto.getQuantity());
        tx.setPricePerUnit(dto.getPricePerUnit());
        tx.setTotalAmount(dto.getQuantity().multiply(dto.getPricePerUnit()));
        tx.setNotes(dto.getNotes());

        transactionRepository.save(tx);

        // Update or create holding
        if ("BUY".equals(tx.getType())) {
            handleBuy(portfolio, symbol, dto);
        } else if ("SELL".equals(tx.getType())) {
            handleSell(portfolio, symbol, dto);
        }

        return tx;
    }

    private void handleBuy(Portfolio portfolio, String symbol, TransactionDTO dto) {
        Optional<Holding> existingOpt = holdingRepository
                .findByPortfolioIdAndSymbol(portfolio.getId(), symbol);

        if (existingOpt.isPresent()) {
            Holding existing = existingOpt.get();
            BigDecimal totalQty = existing.getQuantity().add(dto.getQuantity());
            BigDecimal totalCost = existing.getAverageBuyPrice()
                    .multiply(existing.getQuantity())
                    .add(dto.getPricePerUnit().multiply(dto.getQuantity()));
            BigDecimal newAvg = totalCost.divide(totalQty, 4, RoundingMode.HALF_UP);

            existing.setQuantity(totalQty);
            existing.setAverageBuyPrice(newAvg);
            holdingRepository.save(existing);
        } else {
            Holding holding = new Holding();
            holding.setPortfolio(portfolio);
            holding.setSymbol(symbol);
            holding.setQuantity(dto.getQuantity());
            holding.setAverageBuyPrice(dto.getPricePerUnit());
            holdingRepository.save(holding);
        }
    }

    private void handleSell(Portfolio portfolio, String symbol, TransactionDTO dto) {
        Holding holding = holdingRepository
                .findByPortfolioIdAndSymbol(portfolio.getId(), symbol)
                .orElseThrow(() -> new RuntimeException("No holding found for symbol: " + symbol));

        BigDecimal newQty = holding.getQuantity().subtract(dto.getQuantity());

        if (newQty.compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("Insufficient quantity to sell");
        } else if (newQty.compareTo(BigDecimal.ZERO) == 0) {
            holdingRepository.delete(holding);
        } else {
            holding.setQuantity(newQty);
            holdingRepository.save(holding);
        }
    }

    public List<Transaction> getTransactionsByPortfolio(Long portfolioId) {
        portfolioService.getPortfolioById(portfolioId); // ownership check
        return transactionRepository.findByPortfolioIdOrderByTransactionDateDesc(portfolioId);
    }

    public List<GainLossResponse> getGainLoss(Long portfolioId) {
        portfolioService.getPortfolioById(portfolioId); // ownership check
        List<Holding> holdings = holdingRepository.findByPortfolioId(portfolioId);

        List<GainLossResponse> result = new ArrayList<>();
        for (Holding holding : holdings) {
            Optional<StockPrice> priceOpt = stockPriceRepository.findBySymbol(holding.getSymbol());
            if (priceOpt.isPresent()) {
                result.add(new GainLossResponse(
                        holding.getSymbol(),
                        holding.getQuantity(),
                        holding.getAverageBuyPrice(),
                        priceOpt.get().getCurrentPrice()
                ));
            }
        }
        return result;
    }
}