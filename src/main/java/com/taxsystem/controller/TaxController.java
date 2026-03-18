package com.taxsystem.controller;

import com.taxsystem.dto.TaxCalculationRequest;
import com.taxsystem.dto.TaxCalculationResponse;
import com.taxsystem.service.TaxCalculationService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tax")
public class TaxController {

    private final TaxCalculationService taxCalculationService;

    public TaxController(TaxCalculationService taxCalculationService) {
        this.taxCalculationService = taxCalculationService;
    }

    @PostMapping("/calculate")
    public TaxCalculationResponse calculate(@Valid @RequestBody TaxCalculationRequest request) {
        return taxCalculationService.calculate(request);
    }
}
