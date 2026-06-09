package com.portfolio.tracker.service;

import com.portfolio.tracker.dto.HoldingDTO;
import com.portfolio.tracker.entity.Holding;
import com.portfolio.tracker.entity.Portfolio;
import com.portfolio.tracker.repository.HoldingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HoldingService {

    private final HoldingRepository holdingRepository;
    private final PortfolioService portfolioService;

    public HoldingService(HoldingRepository holdingRepository,
                          PortfolioService portfolioService) {
        this.holdingRepository = holdingRepository;
        this.portfolioService = portfolioService;
    }

    public Holding addHolding(Long portfolioId, HoldingDTO dto) {
        Portfolio portfolio = portfolioService.getPortfolioById(portfolioId);

        Holding holding = new Holding();
        holding.setPortfolio(portfolio);
        holding.setSymbol(dto.getSymbol().toUpperCase());
        holding.setCompanyName(dto.getCompanyName());
        holding.setQuantity(dto.getQuantity());
        holding.setAverageBuyPrice(dto.getAverageBuyPrice());
        holding.setSector(dto.getSector());

        return holdingRepository.save(holding);
    }

    public List<Holding> getHoldingsByPortfolio(Long portfolioId) {
        portfolioService.getPortfolioById(portfolioId); // ownership check
        return holdingRepository.findByPortfolioId(portfolioId);
    }

    public Holding updateHolding(Long portfolioId, Long holdingId, HoldingDTO dto) {
        portfolioService.getPortfolioById(portfolioId); // ownership check
        Holding holding = holdingRepository.findById(holdingId)
                .orElseThrow(() -> new RuntimeException("Holding not found"));

        holding.setSymbol(dto.getSymbol().toUpperCase());
        holding.setCompanyName(dto.getCompanyName());
        holding.setQuantity(dto.getQuantity());
        holding.setAverageBuyPrice(dto.getAverageBuyPrice());
        holding.setSector(dto.getSector());

        return holdingRepository.save(holding);
    }

    public void deleteHolding(Long portfolioId, Long holdingId) {
        portfolioService.getPortfolioById(portfolioId); // ownership check
        holdingRepository.deleteById(holdingId);
    }
}