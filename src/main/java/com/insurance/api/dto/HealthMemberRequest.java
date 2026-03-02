package com.insurance.api.dto;

import lombok.Data;

/**
 * Objeto de transferencia de datos (DTO) para añadir miembros a pólizas de salud.
 * <p>
 * Contiene los datos necesarios para registrar un nuevo miembro familiar
 * en una póliza de salud existente.
 * </p>
 *
 * @author Insurance API Team
 * @version 1.0.0
 */
@Data
public class HealthMemberRequest {
    
    /** Nombre completo del miembro. */
    private String name;
    
    /** Parentesco con el titular de la póliza. */
    private String relationship;
}
