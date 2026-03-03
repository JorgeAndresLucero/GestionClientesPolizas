package com.insurance.api.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

/**
 * Entidad que representa un beneficiario de una póliza de vida.
 * <p>
 * Un beneficiario es la persona designada para recibir la indemnización
 * de la póliza de vida en caso de fallecimiento del titular.
 * Cada póliza puede tener un máximo de 2 beneficiarios.
 * </p>
 *
 * @author Insurance API Team
 * @version 1.0.0
 * @see LifePolicy
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Beneficiary {

    /** Identificador único del beneficiario. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Nombre completo del beneficiario. */
    private String name;
    
    /** Parentesco o relación con el titular de la póliza. */
    private String relationship;

    /** Póliza de vida a la que pertenece este beneficiario. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "life_policy_id")
    @JsonIgnore
    private LifePolicy lifePolicy;
}