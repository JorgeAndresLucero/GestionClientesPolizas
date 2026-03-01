package com.insurance.api.repository;

import com.insurance.api.domain.LifePolicy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LifePolicyRepository extends JpaRepository<LifePolicy, Long> {
}