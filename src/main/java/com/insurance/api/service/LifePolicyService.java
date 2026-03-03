package com.insurance.api.service;

import com.insurance.api.domain.Client;
import com.insurance.api.domain.LifePolicy;
import com.insurance.api.domain.PolicyType;
import com.insurance.api.dto.LifePolicyRequest;
import com.insurance.api.exception.BusinessException;
import com.insurance.api.repository.LifePolicyRepository;
import com.insurance.api.repository.PolicyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Servicio para la gestión de operaciones de negocio relacionadas con pólizas de vida.
 * <p>
 * Proporciona funcionalidad para crear pólizas de vida, validando que cada cliente
 * solo pueda tener una póliza de vida activa.
 * </p>
 *
 * @author Insurance API Team
 * @version 1.0.0
 * @see LifePolicy
 * @see LifePolicyRepository
 * @see PolicyRepository
 */
@Service
@RequiredArgsConstructor
public class LifePolicyService {

    private final LifePolicyRepository lifePolicyRepository;
    private final PolicyRepository policyRepository;

    /**
     * Crea una nueva póliza de vida para un cliente.
     * <p>
     * Valida que el cliente no tenga ya una póliza de vida activa antes de crearla.
     * </p>
     *
     * @param request objeto {@link LifePolicyRequest} con los datos de la póliza
     * @return la póliza de vida creada
     * @throws BusinessException si el cliente ya tiene una póliza de vida
     */
    public LifePolicy createLifePolicy(LifePolicyRequest request) {

        boolean exists = policyRepository
                .existsByClientIdAndType(request.getClientId(), PolicyType.VIDA);

        if (exists) {
            throw new BusinessException("Client already has a life policy");
        }

        LifePolicy policy = new LifePolicy();
        policy.setType(PolicyType.VIDA);
        policy.setInsuredAmount(request.getInsuredAmount());

        Client client = new Client();
        client.setId(request.getClientId());
        policy.setClient(client);

        return lifePolicyRepository.save(policy);
    }
}