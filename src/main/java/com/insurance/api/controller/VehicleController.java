package com.insurance.api.controller;

import com.insurance.api.domain.Vehicle;
import com.insurance.api.domain.VehiclePolicy;
import com.insurance.api.dto.VehiclePolicyRequest;
import com.insurance.api.dto.VehicleRequest;
import com.insurance.api.service.VehicleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para la gestión de pólizas de vehículos.
 * <p>
 * Proporciona endpoints para crear pólizas de vehículos y añadir vehículos
 * a las pólizas existentes.
 * </p>
 *
 * @author Insurance API Team
 * @version 1.0.0
 * @see VehicleService
 * @see VehiclePolicy
 * @see Vehicle
 */
@RestController
@RequestMapping("/policies/vehicle")
@RequiredArgsConstructor
@Tag(name = "Pólizas de Vehículos", description = "API para la gestión de pólizas de seguro de vehículos")
public class VehicleController {

    private final VehicleService vehicleService;

    /**
     * Crea una nueva póliza de vehículo para un cliente.
     *
     * @param request objeto {@link VehiclePolicyRequest} con los datos de la póliza
     * @return la póliza de vehículo creada
     */
    @PostMapping
    @Operation(summary = "Crear póliza de vehículo", description = "Crea una nueva póliza de vehículo para un cliente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Póliza de vehículo creada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public VehiclePolicy createVehiclePolicy(@RequestBody VehiclePolicyRequest request) {
        return vehicleService.createVehiclePolicy(request);
    }

    /**
     * Añade un nuevo vehículo a una póliza de vehículo existente.
     *
     * @param policyId identificador único de la póliza de vehículo
     * @param request objeto {@link VehicleRequest} con los datos del vehículo
     * @return el vehículo añadido a la póliza
     * @throws com.insurance.api.exception.BusinessException si no se encuentra la póliza o es de tipo inválido
     */
    @PostMapping("/{policyId}/vehicles")
    @Operation(summary = "Añadir vehículo a póliza", description = "Añade un vehículo a una póliza de vehículo existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Vehículo añadido exitosamente"),
        @ApiResponse(responseCode = "400", description = "Póliza no encontrada o tipo de póliza inválido")
    })
    public Vehicle addVehicle(
            @Parameter(description = "ID de la póliza de vehículo", example = "1")
            @PathVariable Long policyId,
            @RequestBody VehicleRequest request) {
        return vehicleService.addVehicle(policyId, request);
    }
}