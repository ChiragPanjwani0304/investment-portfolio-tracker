package com.portfolio.tracker.repository;
import com.portfolio.tracker.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByPortfolioIdOrderByTransactionDateDesc(Long portfolioId);
    List<Transaction> findByPortfolioIdAndSymbol(Long portfolioId, String symbol);
}