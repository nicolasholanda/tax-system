package com.taxsystem.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public class TaxCalculationRequest {

    @NotNull
    private Long productId;

    @NotNull
    private Long stateId;

    @NotNull
    private Integer year;

    @NotNull
    @Positive
    private BigDecimal amount;

    public TaxCalculationRequest() {
    }

    public TaxCalculationRequest(Long productId, Long stateId, Integer year, BigDecimal amount) {
        this.productId = productId;
        this.stateId = stateId;
        this.year = year;
        this.amount = amount;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getStateId() {
        return stateId;
    }

    public void setStateId(Long stateId) {
        this.stateId = stateId;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
