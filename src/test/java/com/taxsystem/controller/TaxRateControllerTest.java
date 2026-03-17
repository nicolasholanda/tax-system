package com.taxsystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taxsystem.entity.Product;
import com.taxsystem.entity.State;
import com.taxsystem.entity.TaxRate;
import com.taxsystem.service.TaxRateService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaxRateController.class)
class TaxRateControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TaxRateService taxRateService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private TaxRate createTaxRate() {
        Product product = new Product("Electronics", "Tech");
        product.setId(1L);

        State state = new State("São Paulo", "SP");
        state.setId(1L);

        TaxRate taxRate = new TaxRate(product, state, 2026, new BigDecimal("18.50"));
        taxRate.setId(1L);
        return taxRate;
    }

    @Test
    void findAll_returnsTaxRateList() throws Exception {
        when(taxRateService.findAll()).thenReturn(List.of(createTaxRate()));

        mockMvc.perform(get("/api/tax-rates"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].year").value(2026))
                .andExpect(jsonPath("$[0].rate").value(18.50));
    }

    @Test
    void findById_returnsTaxRate() throws Exception {
        when(taxRateService.findById(1L)).thenReturn(createTaxRate());

        mockMvc.perform(get("/api/tax-rates/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.year").value(2026));
    }

    @Test
    void create_validTaxRate_returnsCreated() throws Exception {
        TaxRate taxRate = createTaxRate();
        when(taxRateService.create(any(TaxRate.class))).thenReturn(taxRate);

        mockMvc.perform(post("/api/tax-rates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taxRate)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void create_invalidTaxRate_returnsBadRequest() throws Exception {
        mockMvc.perform(post("/api/tax-rates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"year\":null,\"rate\":null}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void update_validTaxRate_returnsUpdated() throws Exception {
        TaxRate taxRate = createTaxRate();
        when(taxRateService.update(eq(1L), any(TaxRate.class))).thenReturn(taxRate);

        mockMvc.perform(put("/api/tax-rates/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taxRate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rate").value(18.50));
    }

    @Test
    void delete_returnsNoContent() throws Exception {
        mockMvc.perform(delete("/api/tax-rates/1"))
                .andExpect(status().isNoContent());
    }
}
