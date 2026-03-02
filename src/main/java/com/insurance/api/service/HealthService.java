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

@Service
@RequiredArgsConstructor
public class HealthService {
    private final HealthPolicyRepository healthPolicyRepository;
    private final HealthMemberRepository healthMemberRepository;

    public HealthPolicy createHealthPolicy(HealthPolicyRequest request) {

        HealthPolicy policy = new HealthPolicy();
        policy.setType(PolicyType.SALUD);
        policy.setCoversClientOnly(request.getCoversClientOnly());

        Client client = new Client();
        client.setId(request.getClientId());
        policy.setClient(client);

        return healthPolicyRepository.save(policy);
    }

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
