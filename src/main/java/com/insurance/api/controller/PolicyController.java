package com.insurance.api.controller;
import com.insurance.api.domain.Policy;
import com.insurance.api.service.PolicyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/policies")
@RequiredArgsConstructor
public class PolicyController {

    private final PolicyService policyService;

    @GetMapping("/client/{id}")
    public List<Policy> getPolicies(@PathVariable Long id) {
        return policyService.getPoliciesByClient(id);
    }

    @GetMapping("/{id}")
    public Policy getPolicy(@PathVariable Long id) {
        return policyService.getPolicy(id);
    }

    @DeleteMapping("/{id}")
    public void deletePolicy(@PathVariable Long id) {
        policyService.deletePolicy(id);
    }
}