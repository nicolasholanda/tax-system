package com.taxsystem.repository;

import com.taxsystem.entity.TaxRate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TaxRateRepository extends JpaRepository<TaxRate, Long> {

    Optional<TaxRate> findByProductIdAndStateIdAndYear(Long productId, Long stateId, Integer year);
}
