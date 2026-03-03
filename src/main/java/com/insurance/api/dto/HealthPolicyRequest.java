package com.insurance.api.dto;

import lombok.Data;

/**
 * Objeto de transferencia de datos (DTO) para la creación de pólizas de salud.
 * <p>
 * Contiene los datos necesarios para registrar una nueva póliza de salud,
 * incluyendo el cliente y la configuración de cobertura.
 * </p>
 *
 * @author Insurance API Team
 * @version 1.0.0
 */
@Data
public class HealthPolicyRequest {
    
    /** Identificador del cliente titular. */
    private Long clientId;
    
    /** Indica si la póliza cubre únicamente al cliente titular. */
    private Boolean coversClientOnly;
}
