package com.insurance.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class VehicleRequest {

    @NotBlank
    private String plate;

    private String brand;
    private String model;

    @NotNull
    private Integer vehicleYear;
}