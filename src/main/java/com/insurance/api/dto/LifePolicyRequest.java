package com.insurance.api.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

/**
 * Objeto de transferencia de datos (DTO) para la creación de pólizas de vida.
 * <p>
 * Contiene los datos necesarios para registrar una nueva póliza de vida,
 * incluyendo el cliente y el monto asegurado.
 * </p>
 *
 * @author Insurance API Team
 * @version 1.0.0
 */
@Data
public class LifePolicyRequest {

    /** Identificador del cliente titular. No puede ser nulo. */
    @NotNull
    private Long clientId;

    /** Monto asegurado de la póliza. Debe ser un valor positivo. */
    @NotNull
    @Positive
    private Double insuredAmount;
}