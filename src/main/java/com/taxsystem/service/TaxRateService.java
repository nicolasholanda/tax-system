package com.taxsystem.service;

import com.taxsystem.entity.TaxRate;
import com.taxsystem.exception.ResourceNotFoundException;
import com.taxsystem.repository.TaxRateRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaxRateService {

    private final TaxRateRepository taxRateRepository;

    public TaxRateService(TaxRateRepository taxRateRepository) {
        this.taxRateRepository = taxRateRepository;
    }

    public List<TaxRate> findAll() {
        return taxRateRepository.findAll();
    }

    public TaxRate findById(Long id) {
        return taxRateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TaxRate not found with id: " + id));
    }

    public TaxRate create(TaxRate taxRate) {
        return taxRateRepository.save(taxRate);
    }

    public TaxRate update(Long id, TaxRate taxRate) {
        TaxRate existing = findById(id);
        existing.setProduct(taxRate.getProduct());
        existing.setState(taxRate.getState());
        existing.setYear(taxRate.getYear());
        existing.setRate(taxRate.getRate());
        return taxRateRepository.save(existing);
    }

    public void delete(Long id) {
        TaxRate existing = findById(id);
        taxRateRepository.delete(existing);
    }
}
