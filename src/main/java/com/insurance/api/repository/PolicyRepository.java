package com.insurance.api.repository;

import com.insurance.api.domain.Policy;
import com.insurance.api.domain.PolicyType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repositorio para la gestión de entidades {@link Policy}.
 * <p>
 * Proporciona operaciones CRUD y métodos de consulta personalizados
 * para acceder a los datos de las pólizas en la base de datos.
 * Permite consultar pólizas por cliente y tipo.
 * </p>
 *
 * @author Insurance API Team
 * @version 1.0.0
 * @see Policy
 * @see PolicyType
 */
public interface PolicyRepository extends JpaRepository<Policy, Long> {

    /**
     * Verifica si un cliente tiene alguna póliza registrada.
     *
     * @param clientId identificador único del cliente
     * @return true si el cliente tiene pólizas, false en caso contrario
     */
    boolean existsByClientId(Long clientId);
    
    /**
     * Verifica si un cliente tiene una póliza de un tipo específico.
     *
     * @param clientId identificador único del cliente
     * @param type tipo de póliza a verificar
     * @return true si el cliente tiene una póliza de ese tipo, false en caso contrario
     */
    boolean existsByClientIdAndType(Long clientId, PolicyType type);
    
    /**
     * Busca todas las pólizas asociadas a un cliente específico.
     *
     * @param clientId identificador único del cliente
     * @return lista de pólizas del cliente
     */
    List<Policy> findByClientId(Long clientId);
}