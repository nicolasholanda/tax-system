package com.taxsystem;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.taxsystem.dto.TaxCalculationRequest;
import com.taxsystem.entity.Product;
import com.taxsystem.entity.State;
import com.taxsystem.entity.TaxRate;
import com.taxsystem.repository.ProductRepository;
import com.taxsystem.repository.StateRepository;
import com.taxsystem.repository.TaxRateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class TaxSystemIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private StateRepository stateRepository;

    @Autowired
    private TaxRateRepository taxRateRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        taxRateRepository.deleteAll();
        productRepository.deleteAll();
        stateRepository.deleteAll();
    }

    @Test
    void createProduct_returnsCreated() throws Exception {
        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Laptop\",\"category\":\"Electronics\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Laptop"));
    }

    @Test
    void getProducts_returnsPersistedList() throws Exception {
        productRepository.save(new Product("Laptop", "Electronics"));
        productRepository.save(new Product("Rice", "Food"));

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void createState_returnsCreated() throws Exception {
        mockMvc.perform(post("/api/states")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"São Paulo\",\"abbreviation\":\"SP\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.abbreviation").value("SP"));
    }

    @Test
    void createTaxRate_returnsCreated() throws Exception {
        Product product = productRepository.save(new Product("Laptop", "Electronics"));
        State state = stateRepository.save(new State("São Paulo", "SP"));

        TaxRate taxRate = new TaxRate(product, state, 2026, new BigDecimal("18.50"));

        mockMvc.perform(post("/api/tax-rates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taxRate)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.rate").value(18.50));
    }

    @Test
    void calculateTax_returnsCorrectAmounts() throws Exception {
        Product product = productRepository.save(new Product("Laptop", "Electronics"));
        State state = stateRepository.save(new State("São Paulo", "SP"));
        taxRateRepository.save(new TaxRate(product, state, 2026, new BigDecimal("10.00")));

        TaxCalculationRequest request = new TaxCalculationRequest(
                product.getId(), state.getId(), 2026, new BigDecimal("1000.00"));

        mockMvc.perform(post("/api/tax/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.taxAmount").value(100.00))
                .andExpect(jsonPath("$.total").value(1100.00));
    }

    @Test
    void getProduct_notFound_returns404WithMessage() throws Exception {
        mockMvc.perform(get("/api/products/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void createProduct_invalidRequest_returns400WithErrors() throws Exception {
        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"\",\"category\":\"\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.errors").isArray());
    }

    @Test
    void calculateTax_rateNotFound_returns404WithMessage() throws Exception {
        Product product = productRepository.save(new Product("Laptop", "Electronics"));
        State state = stateRepository.save(new State("São Paulo", "SP"));

        TaxCalculationRequest request = new TaxCalculationRequest(
                product.getId(), state.getId(), 2026, new BigDecimal("1000.00"));

        mockMvc.perform(post("/api/tax/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").exists());
    }
}
