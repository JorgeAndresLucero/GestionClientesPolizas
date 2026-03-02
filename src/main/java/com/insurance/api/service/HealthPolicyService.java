package com.insurance.api.service;

import com.insurance.api.domain.Client;
import com.insurance.api.domain.HealthMember;
import com.insurance.api.domain.HealthPolicy;
import com.insurance.api.domain.PolicyType;
import com.insurance.api.dto.HealthMemberRequest;
import com.insurance.api.dto.HealthPolicyRequest;
import com.insurance.api.exception.BusinessException;
import com.insurance.api.repository.HealthMemberRepository;
import com.insurance.api.repository.HealthPolicyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Servicio para la gestión de operaciones de negocio relacionadas con pólizas de salud.
 * <p>
 * Proporciona funcionalidad para crear pólizas de salud y gestionar los miembros
 * asociados a cada póliza (titular y familiares).
 * </p>
 *
 * @author Insurance API Team
 * @version 1.0.0
 * @see HealthPolicy
 * @see HealthMember
 * @see HealthPolicyRepository
 * @see HealthMemberRepository
 */
@Service
@RequiredArgsConstructor
public class HealthPolicyService {

    private final HealthPolicyRepository repository;
    private final HealthMemberRepository memberRepository;

    /**
     * Obtiene la lista de miembros asociados a una póliza de salud.
     *
     * @param policyId identificador único de la póliza de salud
     * @return lista de miembros de la póliza
     * @throws BusinessException si no se encuentra la póliza
     */
    public List<HealthMember> getMembers(Long policyId) {
        HealthPolicy policy = repository.findById(policyId)
                .orElseThrow(() -> new BusinessException("Health policy not found"));
        return memberRepository.findByHealthPolicyId(policyId);
    }

    /**
     * Añade un nuevo miembro a una póliza de salud existente.
     * <p>
     * Valida que la póliza permita añadir miembros adicionales
     * (no sea solo para el titular).
     * </p>
     *
     * @param policyId identificador único de la póliza de salud
     * @param request objeto {@link HealthMemberRequest} con los datos del miembro
     * @return el miembro añadido a la póliza
     * @throws BusinessException si la póliza solo cubre al titular
     */
    public HealthMember addMember(Long policyId, HealthMemberRequest request) {

        HealthPolicy policy = repository.findById(policyId)
                .orElseThrow(() -> new BusinessException("Health policy not found"));

        if (policy.getCoversClientOnly()) {
            throw new BusinessException("Policy only covers the client");
        }

        HealthMember member = new HealthMember();
        member.setName(request.getName());
        member.setRelationship(request.getRelationship());
        member.setHealthPolicy(policy);

        return memberRepository.save(member);
    }

    /**
     * Crea una nueva póliza de salud para un cliente.
     *
     * @param request objeto {@link HealthPolicyRequest} con los datos de la póliza
     * @return la póliza de salud creada
     */
    public HealthPolicy create(HealthPolicyRequest request) {

        HealthPolicy policy = new HealthPolicy();
        policy.setType(PolicyType.SALUD);
        policy.setCoversClientOnly(request.getCoversClientOnly());

        Client client = new Client();
        client.setId(request.getClientId());
        policy.setClient(client);

        return repository.save(policy);
    }
}
