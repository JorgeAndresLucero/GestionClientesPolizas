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

/**
 * Servicio para la gestión de operaciones de negocio relacionadas con pólizas de salud.
 * <p>
 * Proporciona funcionalidad para crear pólizas de salud y añadir miembros familiares.
 * Incluye validaciones específicas:
 * <ul>
 *   <li>Máximo 2 padres (PADRE/MADRE) por póliza</li>
 *   <li>Solo una esposa (ESPOSA) por póliza</li>
 *   <li>La póliza debe permitir miembros adicionales (no solo titular)</li>
 * </ul>
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
public class HealthService {
    private final HealthPolicyRepository healthPolicyRepository;
    private final HealthMemberRepository healthMemberRepository;

    /**
     * Crea una nueva póliza de salud para un cliente.
     *
     * @param request objeto {@link HealthPolicyRequest} con los datos de la póliza
     * @return la póliza de salud creada
     */
    public HealthPolicy createHealthPolicy(HealthPolicyRequest request) {

        HealthPolicy policy = new HealthPolicy();
        policy.setType(PolicyType.SALUD);
        policy.setCoversClientOnly(request.getCoversClientOnly());

        Client client = new Client();
        client.setId(request.getClientId());
        policy.setClient(client);

        return healthPolicyRepository.save(policy);
    }

    /**
     * Añade un nuevo miembro a una póliza de salud existente.
     * <p>
     * Valida las siguientes reglas de negocio:
     * <ul>
     *   <li>La póliza debe permitir miembros adicionales (no solo titular)</li>
     *   <li>Máximo 2 padres (PADRE o MADRE) por póliza</li>
     *   <li>Solo una esposa (ESPOSA) por póliza</li>
     * </ul>
     * </p>
     *
     * @param policyId identificador único de la póliza de salud
     * @param request objeto {@link HealthMemberRequest} con los datos del miembro
     * @return el miembro añadido a la póliza
     * @throws BusinessException si la póliza no existe, es solo para titular,
     *         o se violan las reglas de parentesco
     */
    public HealthMember addMember(Long policyId, HealthMemberRequest request) {

        HealthPolicy policy = healthPolicyRepository.findById(policyId)
                .orElseThrow(() -> new BusinessException("Health policy not found"));

        if (policy.getCoversClientOnly()) {
            throw new BusinessException("This policy only covers the client");
        }

        long parents = policy.getMembers().stream()
                .filter(m -> m.getRelationship().equals("PADRE")
                        || m.getRelationship().equals("MADRE"))
                .count();

        if ((request.getRelationship().equals("PADRE")
                || request.getRelationship().equals("MADRE")) && parents >= 2) {
            throw new BusinessException("Only 2 parents allowed");
        }

        boolean spouseExists = policy.getMembers().stream()
                .anyMatch(m -> m.getRelationship().equals("ESPOSA"));

        if (request.getRelationship().equals("ESPOSA") && spouseExists) {
            throw new BusinessException("Only one spouse allowed");
        }

        HealthMember member = new HealthMember();
        member.setName(request.getName());
        member.setRelationship(request.getRelationship());
        member.setHealthPolicy(policy);

        return healthMemberRepository.save(member);
    }
}
