package com.portfolio.tracker.service;

import com.portfolio.tracker.entity.User;
import com.portfolio.tracker.entity.WatchList;
import com.portfolio.tracker.repository.UserRepository;
import com.portfolio.tracker.repository.WatchListRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WatchListService {

    private final WatchListRepository watchListRepository;
    private final UserRepository userRepository;

    public WatchListService(WatchListRepository watchListRepository,
                            UserRepository userRepository) {
        this.watchListRepository = watchListRepository;
        this.userRepository = userRepository;
    }

    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public WatchList addToWatchList(String symbol, String companyName) {
        User user = getCurrentUser();

        if (watchListRepository.existsByUserIdAndSymbol(user.getId(), symbol.toUpperCase())) {
            throw new RuntimeException("Symbol already in watchlist");
        }

        WatchList item = new WatchList();
        item.setUser(user);
        item.setSymbol(symbol.toUpperCase());
        item.setCompanyName(companyName);
        return watchListRepository.save(item);
    }

    public List<WatchList> getMyWatchList() {
        return watchListRepository.findByUserId(getCurrentUser().getId());
    }

    public void removeFromWatchList(Long id) {
        watchListRepository.deleteById(id);
    }
}