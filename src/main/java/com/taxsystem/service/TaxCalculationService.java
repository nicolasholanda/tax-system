package com.taxsystem.service;

import com.taxsystem.dto.TaxCalculationRequest;
import com.taxsystem.dto.TaxCalculationResponse;
import com.taxsystem.entity.TaxRate;
import com.taxsystem.repository.TaxRateRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class TaxCalculationService {

    private final TaxRateRepository taxRateRepository;

    public TaxCalculationService(TaxRateRepository taxRateRepository) {
        this.taxRateRepository = taxRateRepository;
    }

    public TaxCalculationResponse calculate(TaxCalculationRequest request) {
        TaxRate taxRate = taxRateRepository
                .findByProductIdAndStateIdAndYear(request.getProductId(), request.getStateId(), request.getYear())
                .orElseThrow(() -> new RuntimeException("TaxRate not found for the given product, state, and year"));

        BigDecimal taxAmount = request.getAmount()
                .multiply(taxRate.getRate())
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

        BigDecimal total = request.getAmount().add(taxAmount);

        return new TaxCalculationResponse(taxAmount, total);
    }
}
