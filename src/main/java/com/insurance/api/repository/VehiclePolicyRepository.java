package com.insurance.api.repository;

import com.insurance.api.domain.VehiclePolicy;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio para la gestión de entidades {@link VehiclePolicy}.
 * <p>
 * Proporciona operaciones CRUD y métodos de consulta personalizados
 * para acceder a los datos de las pólizas de vehículos en la base de datos.
 * </p>
 *
 * @author Insurance API Team
 * @version 1.0.0
 * @see VehiclePolicy
 */
public interface VehiclePolicyRepository extends JpaRepository<VehiclePolicy, Long> {
}