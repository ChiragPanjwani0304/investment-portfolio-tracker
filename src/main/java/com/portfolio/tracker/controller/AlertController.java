package com.portfolio.tracker.controller;

import com.portfolio.tracker.dto.PriceAlertDTO;
import com.portfolio.tracker.entity.PriceAlert;
import com.portfolio.tracker.service.AlertService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alerts")
public class AlertController {

    private final AlertService alertService;

    public AlertController(AlertService alertService) {
        this.alertService = alertService;
    }

    @PostMapping
    public ResponseEntity<PriceAlert> create(@Valid @RequestBody PriceAlertDTO dto) {
        return ResponseEntity.ok(alertService.createAlert(
                dto.getSymbol(), dto.getTargetPrice(), dto.getConditionType()));
    }

    @GetMapping
    public ResponseEntity<List<PriceAlert>> getAll() {
        return ResponseEntity.ok(alertService.getMyAlerts());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        alertService.deleteAlert(id);
        return ResponseEntity.ok("Alert deleted");
    }
}