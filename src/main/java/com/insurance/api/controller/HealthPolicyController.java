package com.insurance.api.controller;

import com.insurance.api.domain.HealthMember;
import com.insurance.api.domain.HealthPolicy;
import com.insurance.api.dto.HealthMemberRequest;
import com.insurance.api.dto.HealthPolicyRequest;
import com.insurance.api.service.HealthPolicyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/policies/health")
@RequiredArgsConstructor
public class HealthPolicyController {

    private final HealthPolicyService healthPolicyService;

    @GetMapping("/{policyId}/members")
    public List<HealthMember> getMembers(@PathVariable Long policyId) {
        return healthPolicyService.getMembers(policyId);
    }

    @PostMapping("/{policyId}/members")
    public HealthMember addMember(
            @PathVariable Long policyId,
            @RequestBody HealthMemberRequest request) {

        return healthPolicyService.addMember(policyId, request);
    }

    @PostMapping
    public HealthPolicy create(@RequestBody HealthPolicyRequest request) {
        return healthPolicyService.create(request);
    }

}