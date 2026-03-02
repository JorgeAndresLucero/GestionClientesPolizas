package com.insurance.api.repository;

import com.insurance.api.domain.HealthMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repositorio para la gestión de entidades {@link HealthMember}.
 * <p>
 * Proporciona operaciones CRUD y métodos de consulta personalizados
 * para acceder a los datos de los miembros de pólizas de salud en la base de datos.
 * </p>
 *
 * @author Insurance API Team
 * @version 1.0.0
 * @see HealthMember
 */
public interface HealthMemberRepository extends JpaRepository<HealthMember, Long> {

    /**
     * Busca todos los miembros asociados a una póliza de salud específica.
     *
     * @param policyId identificador único de la póliza de salud
     * @return lista de miembros de la póliza
     */
    List<HealthMember> findByHealthPolicyId(Long policyId);
}
