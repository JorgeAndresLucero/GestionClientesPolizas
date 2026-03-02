package com.insurance.api.repository;

import com.insurance.api.domain.Client;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio para la gestión de entidades {@link Client}.
 * <p>
 * Proporciona operaciones CRUD y métodos de consulta personalizados
 * para acceder a los datos de los clientes en la base de datos.
 * </p>
 *
 * @author Insurance API Team
 * @version 1.0.0
 * @see Client
 */
public interface ClientRepository extends JpaRepository<Client, Long> {
}