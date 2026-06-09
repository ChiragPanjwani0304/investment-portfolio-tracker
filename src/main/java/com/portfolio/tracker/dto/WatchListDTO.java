package com.portfolio.tracker.dto;

import jakarta.validation.constraints.NotBlank;

public class WatchListDTO {

    @NotBlank
    private String symbol;

    private String companyName;

    public String getSymbol() { return symbol; }
    public void setSymbol(String symbol) { this.symbol = symbol; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
}