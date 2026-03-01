package com.insurance.api.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class LifePolicyRequest {

    @NotNull
    private Long clientId;

    @NotNull
    @Positive
    private Double insuredAmount;
}