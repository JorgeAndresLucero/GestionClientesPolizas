package com.insurance.api.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String plate;
    private String brand;
    private String model;
    private Integer vehicleYear;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_policy_id")
    @JsonBackReference
    private VehiclePolicy vehiclePolicy;
}