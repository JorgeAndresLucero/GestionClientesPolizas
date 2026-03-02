package com.insurance.api.service;

import com.insurance.api.domain.*;
import com.insurance.api.exception.BusinessException;
import com.insurance.api.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PolicyService {

    private final PolicyRepository policyRepository;

    public List<Policy> getPoliciesByClient(Long clientId) {
        return policyRepository.findByClientId(clientId);
    }

    public Policy getPolicy(Long id) {
        return policyRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Policy not found"));
    }

    public void deletePolicy(Long id) {
        if (!policyRepository.existsById(id)) {
            throw new BusinessException("Policy not found");
        }
        policyRepository.deleteById(id);
    }
}