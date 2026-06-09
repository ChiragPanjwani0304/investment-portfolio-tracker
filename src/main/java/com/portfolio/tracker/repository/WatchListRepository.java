package com.portfolio.tracker.repository;
import com.portfolio.tracker.entity.WatchList;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface WatchListRepository extends JpaRepository<WatchList, Long> {
    List<WatchList> findByUserId(Long userId);
    boolean existsByUserIdAndSymbol(Long userId, String symbol);
}