package com.insurance.api.dto;

import lombok.Data;

/**
 * Objeto de transferencia de datos (DTO) para la creación de pólizas de vehículos.
 * <p>
 * Contiene los datos necesarios para registrar una nueva póliza de vehículos.
 * </p>
 *
 * @author Insurance API Team
 * @version 1.0.0
 */
@Data
public class VehiclePolicyRequest {
    
    /** Identificador del cliente titular. */
    private Long clientId;
}