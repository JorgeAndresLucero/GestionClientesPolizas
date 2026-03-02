package com.insurance.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * Objeto de transferencia de datos (DTO) para añadir vehículos a pólizas.
 * <p>
 * Contiene los datos necesarios para registrar un nuevo vehículo
 * en una póliza de vehículos existente.
 * </p>
 *
 * @author Insurance API Team
 * @version 1.0.0
 */
@Data
public class VehicleRequest {

    /** Placa o matrícula del vehículo. No puede estar vacía. */
    @NotBlank
    private String plate;

    /** Marca del vehículo. */
    private String brand;
    
    /** Modelo del vehículo. */
    private String model;

    /** Año de fabricación del vehículo. No puede ser nulo. */
    @NotNull
    private Integer vehicleYear;
}