package com.insurance.api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class BeneficiaryRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String relationship;
}