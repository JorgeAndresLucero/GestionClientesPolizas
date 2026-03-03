package com.insurance.api.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

/**
 * Entidad que representa un cliente del sistema de seguros.
 * <p>
 * Un cliente es una persona que puede contratar pólizas de seguro.
 * Contiene información personal como documento de identidad, nombre,
 * apellidos, correo electrónico, teléfono y fecha de nacimiento.
 * </p>
 *
 * @author Insurance API Team
 * @version 1.0.0
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Client {

    /** Identificador único del cliente. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Tipo de documento de identidad (DNI, NIE, Pasaporte, etc.). */
    private String documentType;
    
    /** Número de documento de identidad. */
    private String documentNumber;
    
    /** Nombre del cliente. */
    private String firstName;
    
    /** Apellidos del cliente. */
    private String lastName;
    
    /** Correo electrónico del cliente. */
    private String email;
    
    /** Número de teléfono del cliente. */
    private String phone;

    /** Fecha de nacimiento del cliente. */
    private LocalDate birthDate;
}