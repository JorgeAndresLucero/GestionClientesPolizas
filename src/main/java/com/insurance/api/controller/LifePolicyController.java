package com.insurance.api.controller;

import com.insurance.api.domain.LifePolicy;
import com.insurance.api.dto.LifePolicyRequest;
import com.insurance.api.service.LifePolicyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/policies/life")
@RequiredArgsConstructor
public class LifePolicyController {

    private final LifePolicyService lifePolicyService;

    @PostMapping
    public LifePolicy create(@RequestBody LifePolicyRequest request) {
        return lifePolicyService.createLifePolicy(request);
    }
}