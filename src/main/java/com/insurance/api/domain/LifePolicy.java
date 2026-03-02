package com.insurance.api.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class LifePolicy extends Policy {

    @OneToMany(mappedBy = "lifePolicy", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Beneficiary> beneficiaries = new ArrayList<>();
    @NotNull
    private Double insuredAmount;
}