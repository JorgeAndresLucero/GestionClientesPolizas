package com.insurance.api.repository;

import com.insurance.api.domain.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    boolean existsByPlate(String plate);
    List<Vehicle> findByVehiclePolicyId(Long policyId);
}