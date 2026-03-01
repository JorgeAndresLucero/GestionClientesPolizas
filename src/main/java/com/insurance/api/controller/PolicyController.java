package com.insurance.api.controller;
import com.insurance.api.dto.LifePolicyRequest;
import jakarta.validation.Valid;
import com.insurance.api.domain.LifePolicy;
import com.insurance.api.service.PolicyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/policies")
@RequiredArgsConstructor
public class PolicyController {

    private final PolicyService service;

    @PostMapping("/life")
    public LifePolicy createLifePolicy(@Valid @RequestBody LifePolicyRequest request) {
        return service.createLifePolicy(request);
    }
}