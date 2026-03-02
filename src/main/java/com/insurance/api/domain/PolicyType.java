package com.insurance.api.domain;

/**
 * Enumeración que representa los tipos de pólizas disponibles en el sistema.
 * <p>
 * Los tipos de póliza son:
 * <ul>
 *   <li>VIDA - Pólizas de seguro de vida</li>
 *   <li>VEHICULO - Pólizas de seguro de vehículos</li>
 *   <li>SALUD - Pólizas de seguro de salud</li>
 * </ul>
 * </p>
 *
 * @author Insurance API Team
 * @version 1.0.0
 */
public enum PolicyType {
    /** Póliza de seguro de vida. */
    VIDA,
    /** Póliza de seguro de vehículos. */
    VEHICULO,
    /** Póliza de seguro de salud. */
    SALUD
}
