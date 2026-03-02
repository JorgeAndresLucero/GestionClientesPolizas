package com.insurance.api.repository;

import com.insurance.api.domain.LifePolicy;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio para la gestión de entidades {@link LifePolicy}.
 * <p>
 * Proporciona operaciones CRUD y métodos de consulta personalizados
 * para acceder a los datos de las pólizas de vida en la base de datos.
 * </p>
 *
 * @author Insurance API Team
 * @version 1.0.0
 * @see LifePolicy
 */
public interface LifePolicyRepository extends JpaRepository<LifePolicy, Long> {
}