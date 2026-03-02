package com.insurance.api.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
public class HealthPolicy extends Policy {

    private Boolean coversClientOnly;

    @OneToMany(mappedBy = "healthPolicy", cascade = CascadeType.ALL)
    private List<HealthMember> members = new ArrayList<>();
}