package com.taxsystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taxsystem.dto.TaxCalculationRequest;
import com.taxsystem.dto.TaxCalculationResponse;
import com.taxsystem.service.TaxCalculationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaxController.class)
class TaxControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TaxCalculationService taxCalculationService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void calculate_validRequest_returnsResponse() throws Exception {
        TaxCalculationResponse response = new TaxCalculationResponse(
                new BigDecimal("20.00"),
                new BigDecimal("220.00")
        );
        when(taxCalculationService.calculate(any(TaxCalculationRequest.class))).thenReturn(response);

        TaxCalculationRequest request = new TaxCalculationRequest(1L, 1L, 2026, new BigDecimal("200.00"));

        mockMvc.perform(post("/api/tax/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.taxAmount").value(20.00))
                .andExpect(jsonPath("$.total").value(220.00));
    }

    @Test
    void calculate_invalidRequest_returnsBadRequest() throws Exception {
        mockMvc.perform(post("/api/tax/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"productId\":null,\"stateId\":null,\"year\":null,\"amount\":null}"))
                .andExpect(status().isBadRequest());
    }
}
