package com.insurance.api.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

/**
 * Entidad que representa un miembro cubierto por una póliza de salud.
 * <p>
 * Los miembros pueden ser el titular de la póliza o sus familiares
 * (cónyuge, hijos, padres). Cada miembro tiene un nombre, parentesco
 * y opcionalmente un costo adicional asociado.
 * </p>
 *
 * @author Insurance API Team
 * @version 1.0.0
 * @see HealthPolicy
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HealthMember {

    /** Identificador único del miembro. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Nombre completo del miembro. */
    private String name;
    
    /** Parentesco con el titular (CLIENT, FATHER, MOTHER, SPOUSE, CHILD). */
    private String relationship;
    
    /** Costo adicional por cubrir a este miembro. */
    private Double extraCost;

    /** Póliza de salud a la que pertenece este miembro. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "health_policy_id")
    @JsonIgnore
    private HealthPolicy healthPolicy;
}