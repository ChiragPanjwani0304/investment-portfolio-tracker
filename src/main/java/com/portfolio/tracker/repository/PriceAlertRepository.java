package com.portfolio.tracker.repository;
import com.portfolio.tracker.entity.PriceAlert;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PriceAlertRepository extends JpaRepository<PriceAlert, Long> {
    List<PriceAlert> findByUserId(Long userId);
    List<PriceAlert> findBySymbolAndIsTriggeredFalse(String symbol);
}