package com.insurance.api.service;

import com.insurance.api.domain.Beneficiary;
import com.insurance.api.domain.Client;
import com.insurance.api.domain.LifePolicy;
import com.insurance.api.dto.BeneficiaryRequest;
import com.insurance.api.exception.BusinessException;
import com.insurance.api.repository.BeneficiaryRepository;
import com.insurance.api.repository.LifePolicyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BeneficiaryService {

    private final BeneficiaryRepository beneficiaryRepository;
    private final LifePolicyRepository lifePolicyRepository;
    public Beneficiary addBeneficiary(Long policyId, BeneficiaryRequest request) {

        LifePolicy policy = lifePolicyRepository.findById(policyId)
                .orElseThrow(() -> new BusinessException("Life policy not found"));

        // Regla: máximo 2 beneficiarios
        if (policy.getBeneficiaries().size() >= 2) {
            throw new BusinessException("Life policy can only have 2 beneficiaries");
        }

        Beneficiary beneficiary = new Beneficiary();
        beneficiary.setName(request.getName());
        beneficiary.setRelationship(request.getRelationship());
        beneficiary.setLifePolicy(policy);

        return beneficiaryRepository.save(beneficiary);
    }

    public List<Beneficiary> getByPolicy(Long policyId) {

        List<Beneficiary> beneficiaries =
                beneficiaryRepository.findByLifePolicyId(policyId);

        if (beneficiaries.isEmpty()) {
            throw new BusinessException("No beneficiaries found for this policy");
        }

        return beneficiaries;
    }

    public List<Beneficiary> findAll() {
        return beneficiaryRepository.findAll();
    }

}