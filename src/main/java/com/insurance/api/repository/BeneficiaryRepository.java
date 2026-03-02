package com.insurance.api.repository;

import com.insurance.api.domain.Beneficiary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repositorio para la gestión de entidades {@link Beneficiary}.
 * <p>
 * Proporciona operaciones CRUD y métodos de consulta personalizados
 * para acceder a los datos de los beneficiarios en la base de datos.
 * </p>
 *
 * @author Insurance API Team
 * @version 1.0.0
 * @see Beneficiary
 */
public interface BeneficiaryRepository extends JpaRepository<Beneficiary, Long> {

    /**
     * Busca todos los beneficiarios asociados a una póliza de vida específica.
     *
     * @param policyId identificador único de la póliza de vida
     * @return lista de beneficiarios de la póliza
     */
    List<Beneficiary> findByLifePolicyId(Long policyId);

}