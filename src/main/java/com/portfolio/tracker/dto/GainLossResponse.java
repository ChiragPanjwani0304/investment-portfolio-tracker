package com.portfolio.tracker.dto;

import java.math.BigDecimal;

public class GainLossResponse {

    private String symbol;
    private BigDecimal quantity;
    private BigDecimal averageBuyPrice;
    private BigDecimal currentPrice;
    private BigDecimal investedValue;
    private BigDecimal currentValue;
    private BigDecimal unrealizedGainLoss;
    private BigDecimal unrealizedGainLossPercent;

    public GainLossResponse(String symbol,
                            BigDecimal quantity,
                            BigDecimal averageBuyPrice,
                            BigDecimal currentPrice) {
        this.symbol = symbol;
        this.quantity = quantity;
        this.averageBuyPrice = averageBuyPrice;
        this.currentPrice = currentPrice;

        this.investedValue = averageBuyPrice.multiply(quantity);
        this.currentValue = currentPrice.multiply(quantity);
        this.unrealizedGainLoss = this.currentValue.subtract(this.investedValue);

        if (this.investedValue.compareTo(BigDecimal.ZERO) != 0) {
            this.unrealizedGainLossPercent = this.unrealizedGainLoss
                    .divide(this.investedValue, 4, java.math.RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100));
        } else {
            this.unrealizedGainLossPercent = BigDecimal.ZERO;
        }
    }

    public String getSymbol() { return symbol; }
    public BigDecimal getQuantity() { return quantity; }
    public BigDecimal getAverageBuyPrice() { return averageBuyPrice; }
    public BigDecimal getCurrentPrice() { return currentPrice; }
    public BigDecimal getInvestedValue() { return investedValue; }
    public BigDecimal getCurrentValue() { return currentValue; }
    public BigDecimal getUnrealizedGainLoss() { return unrealizedGainLoss; }
    public BigDecimal getUnrealizedGainLossPercent() { return unrealizedGainLossPercent; }
}