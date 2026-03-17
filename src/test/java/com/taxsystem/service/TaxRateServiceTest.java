package com.taxsystem.service;

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
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaxRateServiceTest {

    @Mock
    private TaxRateRepository taxRateRepository;

    @InjectMocks
    private TaxRateService taxRateService;

    private TaxRate taxRate;
    private Product product;
    private State state;

    @BeforeEach
    void setUp() {
        product = new Product("Electronics", "Tech");
        product.setId(1L);

        state = new State("São Paulo", "SP");
        state.setId(1L);

        taxRate = new TaxRate(product, state, 2026, new BigDecimal("18.50"));
        taxRate.setId(1L);
    }

    @Test
    void findAll_returnsAllTaxRates() {
        when(taxRateRepository.findAll()).thenReturn(List.of(taxRate));

        List<TaxRate> result = taxRateService.findAll();

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getRate()).isEqualByComparingTo(new BigDecimal("18.50"));
    }

    @Test
    void findById_existingId_returnsTaxRate() {
        when(taxRateRepository.findById(1L)).thenReturn(Optional.of(taxRate));

        TaxRate result = taxRateService.findById(1L);

        assertThat(result.getYear()).isEqualTo(2026);
    }

    @Test
    void findById_nonExistingId_throwsException() {
        when(taxRateRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taxRateService.findById(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("TaxRate not found");
    }

    @Test
    void create_savesAndReturnsTaxRate() {
        when(taxRateRepository.save(any(TaxRate.class))).thenReturn(taxRate);

        TaxRate result = taxRateService.create(new TaxRate(product, state, 2026, new BigDecimal("18.50")));

        assertThat(result.getId()).isEqualTo(1L);
        verify(taxRateRepository).save(any(TaxRate.class));
    }

    @Test
    void update_existingId_updatesAndReturnsTaxRate() {
        when(taxRateRepository.findById(1L)).thenReturn(Optional.of(taxRate));
        when(taxRateRepository.save(any(TaxRate.class))).thenReturn(taxRate);

        TaxRate updated = new TaxRate(product, state, 2027, new BigDecimal("20.00"));
        TaxRate result = taxRateService.update(1L, updated);

        assertThat(result).isNotNull();
        verify(taxRateRepository).save(any(TaxRate.class));
    }

    @Test
    void delete_existingId_deletesTaxRate() {
        when(taxRateRepository.findById(1L)).thenReturn(Optional.of(taxRate));

        taxRateService.delete(1L);

        verify(taxRateRepository).delete(taxRate);
    }
}
