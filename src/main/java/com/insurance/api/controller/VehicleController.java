package com.insurance.api.controller;

import com.insurance.api.domain.Vehicle;
import com.insurance.api.domain.VehiclePolicy;
import com.insurance.api.dto.VehiclePolicyRequest;
import com.insurance.api.dto.VehicleRequest;
import com.insurance.api.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/policies/vehicle")
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleService vehicleService;

    @PostMapping
    public VehiclePolicy createVehiclePolicy(@RequestBody VehiclePolicyRequest request) {
        return vehicleService.createVehiclePolicy(request);
    }

    @PostMapping("/{policyId}/vehicles")
    public Vehicle addVehicle(
            @PathVariable Long policyId,
            @RequestBody VehicleRequest request) {
        return vehicleService.addVehicle(policyId, request);
    }
}