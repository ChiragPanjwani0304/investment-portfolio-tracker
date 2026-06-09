package com.portfolio.tracker.controller;

import com.portfolio.tracker.dto.WatchListDTO;
import com.portfolio.tracker.entity.WatchList;
import com.portfolio.tracker.service.WatchListService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/watchlist")
public class WatchListController {

    private final WatchListService watchListService;

    public WatchListController(WatchListService watchListService) {
        this.watchListService = watchListService;
    }

    @PostMapping
    public ResponseEntity<WatchList> add(@Valid @RequestBody WatchListDTO dto) {
        return ResponseEntity.ok(watchListService.addToWatchList(dto.getSymbol(), dto.getCompanyName()));
    }

    @GetMapping
    public ResponseEntity<List<WatchList>> getAll() {
        return ResponseEntity.ok(watchListService.getMyWatchList());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> remove(@PathVariable Long id) {
        watchListService.removeFromWatchList(id);
        return ResponseEntity.ok("Removed from watchlist");
    }
}