package com.insurance.api.domain;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;


/**
 * Clase base abstracta que representa una póliza de seguro.
 * <p>
 * Esta clase es la entidad padre para todos los tipos de pólizas del sistema:
 * {@link LifePolicy}, {@link HealthPolicy} y {@link VehiclePolicy}.
 * Utiliza herencia de tipo JOINED para mapeo en base de datos.
 * </p>
 *
 * @author Insurance API Team
 * @version 1.0.0
 * @see LifePolicy
 * @see HealthPolicy
 * @see VehiclePolicy
 */
@Entity
@Getter
@Setter
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public abstract class Policy {

    /** Identificador único de la póliza. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Tipo de póliza (VIDA, SALUD, VEHICULO). */
    @Enumerated(EnumType.STRING)
    private PolicyType type;

    /** Cliente titular de la póliza. */
    @ManyToOne
    private Client client;
}