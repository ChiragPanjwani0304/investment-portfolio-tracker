package com.portfolio.tracker.service;

import com.portfolio.tracker.entity.PriceAlert;
import com.portfolio.tracker.entity.StockPrice;
import com.portfolio.tracker.entity.User;
import com.portfolio.tracker.repository.PriceAlertRepository;
import com.portfolio.tracker.repository.StockPriceRepository;
import com.portfolio.tracker.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AlertService {

    private final PriceAlertRepository alertRepository;
    private final StockPriceRepository stockPriceRepository;
    private final UserRepository userRepository;

    public AlertService(PriceAlertRepository alertRepository,
                        StockPriceRepository stockPriceRepository,
                        UserRepository userRepository) {
        this.alertRepository = alertRepository;
        this.stockPriceRepository = stockPriceRepository;
        this.userRepository = userRepository;
    }

    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public PriceAlert createAlert(String symbol, BigDecimal targetPrice, String conditionType) {
        User user = getCurrentUser();
        PriceAlert alert = new PriceAlert();
        alert.setUser(user);
        alert.setSymbol(symbol.toUpperCase());
        alert.setTargetPrice(targetPrice);
        alert.setConditionType(conditionType.toUpperCase());
        alert.setIsTriggered(false);
        return alertRepository.save(alert);
    }

    public List<PriceAlert> getMyAlerts() {
        return alertRepository.findByUserId(getCurrentUser().getId());
    }

    public void deleteAlert(Long id) {
        alertRepository.deleteById(id);
    }

    // Called by scheduler after every price refresh
    public void checkAndTriggerAlerts(String symbol) {
        StockPrice price = stockPriceRepository.findBySymbol(symbol).orElse(null);
        if (price == null) return;

        List<PriceAlert> alerts = alertRepository.findBySymbolAndIsTriggeredFalse(symbol);

        for (PriceAlert alert : alerts) {
            boolean triggered = false;

            if ("ABOVE".equals(alert.getConditionType()) &&
                    price.getCurrentPrice().compareTo(alert.getTargetPrice()) >= 0) {
                triggered = true;
            } else if ("BELOW".equals(alert.getConditionType()) &&
                    price.getCurrentPrice().compareTo(alert.getTargetPrice()) <= 0) {
                triggered = true;
            }

            if (triggered) {
                alert.setIsTriggered(true);
                alert.setTriggeredAt(LocalDateTime.now());
                alertRepository.save(alert);
                System.out.println("ALERT TRIGGERED: " + alert.getSymbol()
                        + " " + alert.getConditionType()
                        + " " + alert.getTargetPrice());
            }
        }
    }
}