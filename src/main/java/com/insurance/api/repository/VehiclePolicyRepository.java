package com.insurance.api.repository;

import com.insurance.api.domain.VehiclePolicy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehiclePolicyRepository extends JpaRepository<VehiclePolicy, Long> {
}