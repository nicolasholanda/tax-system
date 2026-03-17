package com.taxsystem.controller;

import com.taxsystem.entity.TaxRate;
import com.taxsystem.service.TaxRateService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/tax-rates")
public class TaxRateController {

    private final TaxRateService taxRateService;

    public TaxRateController(TaxRateService taxRateService) {
        this.taxRateService = taxRateService;
    }

    @GetMapping
    public List<TaxRate> findAll() {
        return taxRateService.findAll();
    }

    @GetMapping("/{id}")
    public TaxRate findById(@PathVariable Long id) {
        return taxRateService.findById(id);
    }

    @PostMapping
    public ResponseEntity<TaxRate> create(@Valid @RequestBody TaxRate taxRate) {
        return ResponseEntity.status(HttpStatus.CREATED).body(taxRateService.create(taxRate));
    }

    @PutMapping("/{id}")
    public TaxRate update(@PathVariable Long id, @Valid @RequestBody TaxRate taxRate) {
        return taxRateService.update(id, taxRate);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        taxRateService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
