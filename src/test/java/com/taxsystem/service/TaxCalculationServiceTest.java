package com.taxsystem.service;

import com.taxsystem.dto.TaxCalculationRequest;
import com.taxsystem.dto.TaxCalculationResponse;
import com.taxsystem.entity.Product;
import com.taxsystem.entity.State;
import com.taxsystem.entity.TaxRate;
import com.taxsystem.repository.TaxRateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaxCalculationServiceTest {

    @Mock
    private TaxRateRepository taxRateRepository;

    @InjectMocks
    private TaxCalculationService taxCalculationService;

    private TaxRate taxRate;

    @BeforeEach
    void setUp() {
        Product product = new Product("Electronics", "Tech");
        product.setId(1L);

        State state = new State("São Paulo", "SP");
        state.setId(1L);

        taxRate = new TaxRate(product, state, 2026, new BigDecimal("10.00"));
        taxRate.setId(1L);
    }

    @Test
    void calculate_validRequest_returnsCorrectAmounts() {
        when(taxRateRepository.findByProductIdAndStateIdAndYear(1L, 1L, 2026))
                .thenReturn(Optional.of(taxRate));

        TaxCalculationRequest request = new TaxCalculationRequest(1L, 1L, 2026, new BigDecimal("200.00"));
        TaxCalculationResponse response = taxCalculationService.calculate(request);

        assertThat(response.getTaxAmount()).isEqualByComparingTo(new BigDecimal("20.00"));
        assertThat(response.getTotal()).isEqualByComparingTo(new BigDecimal("220.00"));
    }

    @Test
    void calculate_rateNotFound_throwsException() {
        when(taxRateRepository.findByProductIdAndStateIdAndYear(1L, 1L, 2026))
                .thenReturn(Optional.empty());

        TaxCalculationRequest request = new TaxCalculationRequest(1L, 1L, 2026, new BigDecimal("200.00"));

        assertThatThrownBy(() -> taxCalculationService.calculate(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("TaxRate not found");
    }
}
