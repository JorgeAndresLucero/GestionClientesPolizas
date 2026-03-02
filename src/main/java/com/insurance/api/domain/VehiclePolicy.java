package com.insurance.api.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Entidad que representa una póliza de seguro de vehículos.
 * <p>
 * Las pólizas de vehículos proporcionan cobertura para uno o más vehículos
 * pertenecientes al titular de la póliza.
 * </p>
 *
 * @author Insurance API Team
 * @version 1.0.0
 * @see Policy
 * @see Vehicle
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class VehiclePolicy extends Policy {

    /** Lista de vehículos asegurados bajo esta póliza. */
    @OneToMany(mappedBy = "vehiclePolicy", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Vehicle> vehicles = new ArrayList<>();
}