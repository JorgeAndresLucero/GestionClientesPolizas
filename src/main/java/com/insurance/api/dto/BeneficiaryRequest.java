package com.insurance.api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Objeto de transferencia de datos (DTO) para la creación de beneficiarios.
 * <p>
 * Contiene los datos necesarios para registrar un nuevo beneficiario
 * en una póliza de vida.
 * </p>
 *
 * @author Insurance API Team
 * @version 1.0.0
 */
@Data
public class BeneficiaryRequest {

    /** Nombre completo del beneficiario. No puede estar vacío. */
    @NotBlank
    private String name;

    /** Parentesco o relación con el titular. No puede estar vacío. */
    @NotBlank
    private String relationship;
}