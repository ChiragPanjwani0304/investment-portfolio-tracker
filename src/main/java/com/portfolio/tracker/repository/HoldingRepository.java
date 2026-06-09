package com.portfolio.tracker.repository;
import com.portfolio.tracker.entity.Holding;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface HoldingRepository extends JpaRepository<Holding, Long> {
    List<Holding> findByPortfolioId(Long portfolioId);
    Optional<Holding> findByPortfolioIdAndSymbol(Long portfolioId, String symbol);
}