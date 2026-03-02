package com.insurance.api.service;

import com.insurance.api.domain.Client;
import com.insurance.api.domain.PolicyType;
import com.insurance.api.domain.Vehicle;
import com.insurance.api.domain.VehiclePolicy;
import com.insurance.api.dto.VehiclePolicyRequest;
import com.insurance.api.dto.VehicleRequest;
import com.insurance.api.exception.BusinessException;
import com.insurance.api.repository.ClientRepository;
import com.insurance.api.repository.VehiclePolicyRepository;
import com.insurance.api.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.insurance.api.domain.PolicyType.VEHICULO;

@Service
@RequiredArgsConstructor
public class VehicleService {
    private final VehiclePolicyRepository vehiclePolicyRepository;
    private final VehicleRepository vehicleRepository;
    private final ClientRepository clientRepository;
    public Vehicle addVehicle(Long policyId, VehicleRequest request) {

        VehiclePolicy policy = vehiclePolicyRepository.findById(policyId)
                .orElseThrow(() -> new BusinessException("Vehicle policy not found"));

        if (policy.getType() != VEHICULO) {
            throw new BusinessException("Invalid policy type");
        }

        Vehicle vehicle = new Vehicle();
        vehicle.setPlate(request.getPlate());
        vehicle.setBrand(request.getBrand());
        vehicle.setModel(request.getModel());
        vehicle.setVehicleYear(request.getVehicleYear());
        vehicle.setVehiclePolicy(policy);

        return vehicleRepository.save(vehicle);
    }

    public VehiclePolicy createVehiclePolicy(VehiclePolicyRequest request) {

        VehiclePolicy policy = new VehiclePolicy();
        policy.setType(VEHICULO);

        Client client = new Client();
        client.setId(request.getClientId());
        policy.setClient(client);

        return vehiclePolicyRepository.save(policy);
    }
}
