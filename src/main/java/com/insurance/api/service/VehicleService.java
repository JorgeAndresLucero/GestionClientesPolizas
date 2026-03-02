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

/**
 * Servicio para la gestión de operaciones de negocio relacionadas con pólizas de vehículos.
 * <p>
 * Proporciona funcionalidad para crear pólizas de vehículos y añadir vehículos
 * a las pólizas existentes.
 * </p>
 *
 * @author Insurance API Team
 * @version 1.0.0
 * @see VehiclePolicy
 * @see Vehicle
 * @see VehiclePolicyRepository
 * @see VehicleRepository
 */
@Service
@RequiredArgsConstructor
public class VehicleService {
    private final VehiclePolicyRepository vehiclePolicyRepository;
    private final VehicleRepository vehicleRepository;
    private final ClientRepository clientRepository;
    
    /**
     * Añade un nuevo vehículo a una póliza de vehículo existente.
     * <p>
     * Valida que la póliza exista y sea del tipo correcto (VEHICULO).
     * </p>
     *
     * @param policyId identificador único de la póliza de vehículo
     * @param request objeto {@link VehicleRequest} con los datos del vehículo
     * @return el vehículo añadido a la póliza
     * @throws BusinessException si no se encuentra la póliza o es de tipo inválido
     */
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

    /**
     * Crea una nueva póliza de vehículo para un cliente.
     *
     * @param request objeto {@link VehiclePolicyRequest} con los datos de la póliza
     * @return la póliza de vehículo creada
     */
    public VehiclePolicy createVehiclePolicy(VehiclePolicyRequest request) {

        VehiclePolicy policy = new VehiclePolicy();
        policy.setType(VEHICULO);

        Client client = new Client();
        client.setId(request.getClientId());
        policy.setClient(client);

        return vehiclePolicyRepository.save(policy);
    }
}
