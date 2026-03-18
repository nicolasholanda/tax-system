package com.taxsystem.dto;

import java.math.BigDecimal;

public class TaxCalculationResponse {

    private BigDecimal taxAmount;
    private BigDecimal total;

    public TaxCalculationResponse() {
    }

    public TaxCalculationResponse(BigDecimal taxAmount, BigDecimal total) {
        this.taxAmount = taxAmount;
        this.total = total;
    }

    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
}
