package com.insurance.api.dto;

import lombok.Data;

@Data
public class HealthPolicyRequest {
    private Long clientId;
    private Boolean coversClientOnly;
}
