package com.portfolio.tracker.repository;
import com.portfolio.tracker.entity.StockPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface StockPriceRepository extends JpaRepository<StockPrice, Long> {
    Optional<StockPrice> findBySymbol(String symbol);
    List<StockPrice> findBySymbolIn(List<String> symbols);
}