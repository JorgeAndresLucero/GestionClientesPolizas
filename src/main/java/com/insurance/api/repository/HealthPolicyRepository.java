package com.insurance.api.repository;

import com.insurance.api.domain.HealthPolicy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HealthPolicyRepository extends JpaRepository<HealthPolicy, Long> {
}