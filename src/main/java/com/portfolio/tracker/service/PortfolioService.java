package com.portfolio.tracker.service;

import com.portfolio.tracker.dto.PortfolioDTO;
import com.portfolio.tracker.entity.Portfolio;
import com.portfolio.tracker.entity.User;
import com.portfolio.tracker.repository.PortfolioRepository;
import com.portfolio.tracker.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;
    private final UserRepository userRepository;

    public PortfolioService(PortfolioRepository portfolioRepository,
                            UserRepository userRepository) {
        this.portfolioRepository = portfolioRepository;
        this.userRepository = userRepository;
    }

    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public Portfolio createPortfolio(PortfolioDTO dto) {
        User user = getCurrentUser();
        Portfolio portfolio = new Portfolio();
        portfolio.setUser(user);
        portfolio.setName(dto.getName());
        portfolio.setDescription(dto.getDescription());
        return portfolioRepository.save(portfolio);
    }

    public List<Portfolio> getMyPortfolios() {
        User user = getCurrentUser();
        return portfolioRepository.findByUserId(user.getId());
    }

    public Portfolio getPortfolioById(Long id) {
        Portfolio portfolio = portfolioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Portfolio not found"));
        validateOwnership(portfolio);
        return portfolio;
    }

    public Portfolio updatePortfolio(Long id, PortfolioDTO dto) {
        Portfolio portfolio = getPortfolioById(id);
        portfolio.setName(dto.getName());
        portfolio.setDescription(dto.getDescription());
        return portfolioRepository.save(portfolio);
    }

    public void deletePortfolio(Long id) {
        Portfolio portfolio = getPortfolioById(id);
        portfolioRepository.delete(portfolio);
    }

    private void validateOwnership(Portfolio portfolio) {
        User currentUser = getCurrentUser();
        if (!portfolio.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("Access denied");
        }
    }
}