package com.portfolio.tracker.scheduler;

import com.portfolio.tracker.service.AlertService;
import com.portfolio.tracker.service.StockPriceService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
@ConditionalOnProperty(name = "scheduler.enabled", havingValue = "true")
public class PriceRefreshScheduler {

    private final StockPriceService stockPriceService;
    private final AlertService alertService;

    public PriceRefreshScheduler(StockPriceService stockPriceService, AlertService alertService) {
        this.stockPriceService = stockPriceService;
        this.alertService = alertService;
    }

//    // Runs every 15 minutes, Mon-Fri, 9:30 AM - 4:00 PM EST
//    @Scheduled(cron = "0 */15 9-16 * * MON-FRI")
//    public void refreshPrices() {
//        System.out.println("Scheduler: Refreshing stock prices...");
//        stockPriceService.refreshAllHoldingPrices();
//        System.out.println("Scheduler: Price refresh complete.");
//    }
// Fires at 9:15 AM and 12:30 PM IST, Mon-Fri
@Scheduled(cron = "0 15 9 * * MON-FRI", zone = "Asia/Kolkata")
public void refreshAtMarketOpen() {
    System.out.println("Scheduler: Morning refresh...");
    stockPriceService.refreshAllHoldingPrices();
    stockPriceService.getAllCachedPrices()
            .forEach(p -> alertService.checkAndTriggerAlerts(p.getSymbol()));
}

    @Scheduled(cron = "0 30 12 * * MON-FRI", zone = "Asia/Kolkata")
    public void refreshAtMidday() {
        System.out.println("Scheduler: Midday refresh...");
        stockPriceService.refreshAllHoldingPrices();
        stockPriceService.getAllCachedPrices()
                .forEach(p -> alertService.checkAndTriggerAlerts(p.getSymbol()));
    }
}