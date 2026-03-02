package com.insurance.api.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Entidad que representa una póliza de seguro de salud.
 * <p>
 * Las pólizas de salud proporcionan cobertura médica al titular y opcionalmente
 * a sus familiares. La póliza puede configurarse para cubrir solo al titular
 * o permitir la inclusión de miembros adicionales (cónyuge, hijos, padres).
 * </p>
 *
 * @author Insurance API Team
 * @version 1.0.0
 * @see Policy
 * @see HealthMember
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class HealthPolicy extends Policy {

    /** Indica si la póliza cubre únicamente al cliente titular. */
    private Boolean coversClientOnly;

    /** Lista de miembros cubiertos por la póliza de salud. */
    @OneToMany(mappedBy = "healthPolicy", cascade = CascadeType.ALL)
    private List<HealthMember> members = new ArrayList<>();
}