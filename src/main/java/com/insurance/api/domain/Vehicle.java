package com.insurance.api.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

/**
 * Entidad que representa un vehículo asegurado bajo una póliza de vehículos.
 * <p>
 * Contiene información detallada del vehículo incluyendo placa, marca, modelo
 * y año de fabricación.
 * </p>
 *
 * @author Insurance API Team
 * @version 1.0.0
 * @see VehiclePolicy
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vehicle {

    /** Identificador único del vehículo. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Placa o matrícula del vehículo. */
    private String plate;
    
    /** Marca del vehículo. */
    private String brand;
    
    /** Modelo del vehículo. */
    private String model;
    
    /** Año de fabricación del vehículo. */
    private Integer vehicleYear;

    /** Póliza de vehículo a la que pertenece este vehículo. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_policy_id")
    @JsonBackReference
    private VehiclePolicy vehiclePolicy;
}