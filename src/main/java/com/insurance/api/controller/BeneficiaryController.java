package com.insurance.api.controller;

import com.insurance.api.domain.Beneficiary;
import com.insurance.api.dto.BeneficiaryRequest;
import com.insurance.api.service.BeneficiaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/policies")
@RequiredArgsConstructor
public class BeneficiaryController {

    private final BeneficiaryService beneficiaryService;

    @GetMapping("/life/{policyId}/beneficiaries")
    public List<Beneficiary> getBeneficiaries(@PathVariable Long policyId) {
        return beneficiaryService.getByPolicy(policyId);
    }

    @GetMapping("/beneficiaries")
    public List<Beneficiary> findAll() {
        return beneficiaryService.findAll();
    }

    @PostMapping("/life/{policyId}/beneficiaries")
    public Beneficiary addBeneficiary(
            @PathVariable Long policyId,
            @RequestBody BeneficiaryRequest request) {

        return beneficiaryService.addBeneficiary(policyId, request);
    }

}
