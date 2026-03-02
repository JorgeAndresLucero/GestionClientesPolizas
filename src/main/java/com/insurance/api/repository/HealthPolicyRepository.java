package com.insurance.api.repository;

import com.insurance.api.domain.HealthPolicy;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio para la gestión de entidades {@link HealthPolicy}.
 * <p>
 * Proporciona operaciones CRUD y métodos de consulta personalizados
 * para acceder a los datos de las pólizas de salud en la base de datos.
 * </p>
 *
 * @author Insurance API Team
 * @version 1.0.0
 * @see HealthPolicy
 */
public interface HealthPolicyRepository extends JpaRepository<HealthPolicy, Long> {
}