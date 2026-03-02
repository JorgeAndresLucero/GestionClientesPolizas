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

@Service
@RequiredArgsConstructor
public class LifePolicyService {

    private final LifePolicyRepository lifePolicyRepository;
    private final PolicyRepository policyRepository;

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