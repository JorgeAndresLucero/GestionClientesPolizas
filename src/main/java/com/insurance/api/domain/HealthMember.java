package com.insurance.api.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HealthMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String relationship; // CLIENT, FATHER, MOTHER, SPOUSE, CHILD
    private Double extraCost;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "health_policy_id")
    @JsonIgnore
    private HealthPolicy healthPolicy;
}