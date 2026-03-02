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

@Service
@RequiredArgsConstructor
public class HealthPolicyService {

    private final HealthPolicyRepository repository;
    private final HealthMemberRepository memberRepository;

    public List<HealthMember> getMembers(Long policyId) {
        HealthPolicy policy = repository.findById(policyId)
                .orElseThrow(() -> new BusinessException("Health policy not found"));
        return memberRepository.findByHealthPolicyId(policyId);
    }

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
