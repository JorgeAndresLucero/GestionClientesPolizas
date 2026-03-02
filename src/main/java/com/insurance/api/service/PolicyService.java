package com.insurance.api.service;

import com.insurance.api.domain.*;
import com.insurance.api.exception.BusinessException;
import com.insurance.api.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Servicio para la gestión de operaciones de negocio relacionadas con pólizas en general.
 * <p>
 * Proporciona funcionalidad para consultar pólizas por cliente, obtener detalles
 * de una póliza específica y eliminar pólizas del sistema.
 * </p>
 *
 * @author Insurance API Team
 * @version 1.0.0
 * @see Policy
 * @see PolicyRepository
 */
@Service
@RequiredArgsConstructor
public class PolicyService {

    private final PolicyRepository policyRepository;

    /**
     * Obtiene todas las pólizas asociadas a un cliente específico.
     *
     * @param clientId identificador único del cliente
     * @return lista de pólizas del cliente
     */
    public List<Policy> getPoliciesByClient(Long clientId) {
        return policyRepository.findByClientId(clientId);
    }

    /**
     * Obtiene los detalles de una póliza específica por su identificador.
     *
     * @param id identificador único de la póliza
     * @return la póliza encontrada
     * @throws BusinessException si no se encuentra la póliza
     */
    public Policy getPolicy(Long id) {
        return policyRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Policy not found"));
    }

    /**
     * Elimina una póliza del sistema.
     *
     * @param id identificador único de la póliza a eliminar
     * @throws BusinessException si no se encuentra la póliza
     */
    public void deletePolicy(Long id) {
        if (!policyRepository.existsById(id)) {
            throw new BusinessException("Policy not found");
        }
        policyRepository.deleteById(id);
    }
}