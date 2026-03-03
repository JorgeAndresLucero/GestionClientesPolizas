package com.insurance.api.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Entidad que representa una póliza de seguro de vida.
 * <p>
 * Las pólizas de vida proporcionan cobertura financiera a los beneficiarios
 * designados en caso de fallecimiento del titular. Cada póliza puede tener
 * un máximo de 2 beneficiarios y un monto asegurado específico.
 * </p>
 *
 * @author Insurance API Team
 * @version 1.0.0
 * @see Policy
 * @see Beneficiary
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class LifePolicy extends Policy {

    /** Lista de beneficiarios de la póliza de vida. */
    @OneToMany(mappedBy = "lifePolicy", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Beneficiary> beneficiaries = new ArrayList<>();
    
    /** Monto asegurado de la póliza. No puede ser nulo. */
    @NotNull
    private Double insuredAmount;
}