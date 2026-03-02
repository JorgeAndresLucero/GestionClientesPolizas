package com.insurance.api.repository;

import com.insurance.api.domain.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repositorio para la gestión de entidades {@link Vehicle}.
 * <p>
 * Proporciona operaciones CRUD y métodos de consulta personalizados
 * para acceder a los datos de los vehículos en la base de datos.
 * </p>
 *
 * @author Insurance API Team
 * @version 1.0.0
 * @see Vehicle
 */
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    
    /**
     * Verifica si existe un vehículo con una placa específica.
     *
     * @param plate placa o matrícula del vehículo
     * @return true si existe un vehículo con esa placa, false en caso contrario
     */
    boolean existsByPlate(String plate);
    
    /**
     * Busca todos los vehículos asociados a una póliza de vehículo específica.
     *
     * @param policyId identificador único de la póliza de vehículo
     * @return lista de vehículos de la póliza
     */
    List<Vehicle> findByVehiclePolicyId(Long policyId);
}