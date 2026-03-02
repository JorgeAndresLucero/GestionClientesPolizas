package com.insurance.api.controller;
import com.insurance.api.domain.Policy;
import com.insurance.api.service.PolicyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión general de pólizas.
 * <p>
 * Proporciona endpoints para consultar pólizas por cliente, obtener detalles
 * de una póliza específica y eliminar pólizas del sistema.
 * </p>
 *
 * @author Insurance API Team
 * @version 1.0.0
 * @see PolicyService
 * @see Policy
 */
@RestController
@RequestMapping("/policies")
@RequiredArgsConstructor
@Tag(name = "Pólizas", description = "API para la consulta y gestión general de pólizas")
public class PolicyController {

    private final PolicyService policyService;

    /**
     * Obtiene todas las pólizas asociadas a un cliente específico.
     *
     * @param id identificador único del cliente
     * @return lista de pólizas del cliente
     */
    @GetMapping("/client/{id}")
    @Operation(summary = "Listar pólizas por cliente", description = "Obtiene todas las pólizas (vida, salud, vehículo) de un cliente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de pólizas obtenida exitosamente")
    })
    public List<Policy> getPolicies(
            @Parameter(description = "ID del cliente", example = "1")
            @PathVariable Long id) {
        return policyService.getPoliciesByClient(id);
    }

    /**
     * Obtiene los detalles de una póliza específica por su identificador.
     *
     * @param id identificador único de la póliza
     * @return la póliza encontrada
     * @throws com.insurance.api.exception.BusinessException si no se encuentra la póliza
     */
    @GetMapping("/{id}")
    @Operation(summary = "Buscar póliza por ID", description = "Obtiene los detalles de una póliza específica")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Póliza encontrada"),
        @ApiResponse(responseCode = "400", description = "Póliza no encontrada")
    })
    public Policy getPolicy(
            @Parameter(description = "ID de la póliza", example = "1")
            @PathVariable Long id) {
        return policyService.getPolicy(id);
    }

    /**
     * Elimina una póliza del sistema.
     *
     * @param id identificador único de la póliza a eliminar
     * @throws com.insurance.api.exception.BusinessException si no se encuentra la póliza
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar póliza", description = "Elimina una póliza del sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Póliza eliminada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Póliza no encontrada")
    })
    public void deletePolicy(
            @Parameter(description = "ID de la póliza", example = "1")
            @PathVariable Long id) {
        policyService.deletePolicy(id);
    }
}