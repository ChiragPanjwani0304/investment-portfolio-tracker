package com.portfolio.tracker.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "holdings")
public class Holding {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "portfolio_id", nullable = false)
    @JsonIgnore
    private Portfolio portfolio;

    @Column(nullable = false)
    private String symbol;

    @Column(name = "company_name")
    private String companyName;

    @Column(nullable = false, precision = 10, scale = 4)
    private BigDecimal quantity;

    @Column(name = "average_buy_price", nullable = false, precision = 15, scale = 4)
    private BigDecimal averageBuyPrice;

    private String sector;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Portfolio getPortfolio() { return portfolio; }
    public void setPortfolio(Portfolio portfolio) { this.portfolio = portfolio; }

    public String getSymbol() { return symbol; }
    public void setSymbol(String symbol) { this.symbol = symbol; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public BigDecimal getQuantity() { return quantity; }
    public void setQuantity(BigDecimal quantity) { this.quantity = quantity; }

    public BigDecimal getAverageBuyPrice() { return averageBuyPrice; }
    public void setAverageBuyPrice(BigDecimal averageBuyPrice) { this.averageBuyPrice = averageBuyPrice; }

    public String getSector() { return sector; }
    public void setSector(String sector) { this.sector = sector; }
}