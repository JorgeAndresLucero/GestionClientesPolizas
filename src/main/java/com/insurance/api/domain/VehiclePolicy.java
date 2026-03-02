package com.insurance.api.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class VehiclePolicy extends Policy {

    @OneToMany(mappedBy = "vehiclePolicy", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Vehicle> vehicles = new ArrayList<>();
}