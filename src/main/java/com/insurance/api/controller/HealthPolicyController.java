package com.insurance.api.controller;

import com.insurance.api.domain.HealthMember;
import com.insurance.api.domain.HealthPolicy;
import com.insurance.api.dto.HealthMemberRequest;
import com.insurance.api.dto.HealthPolicyRequest;
import com.insurance.api.service.HealthPolicyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de pólizas de salud.
 * <p>
 * Proporciona endpoints para crear pólizas de salud y gestionar los miembros
 * asociados a cada póliza (titular y familiares).
 * </p>
 *
 * @author Insurance API Team
 * @version 1.0.0
 * @see HealthPolicyService
 * @see HealthPolicy
 * @see HealthMember
 */
@RestController
@RequestMapping("/policies/health")
@RequiredArgsConstructor
@Tag(name = "Pólizas de Salud", description = "API para la gestión de pólizas de seguro de salud y sus miembros")
public class HealthPolicyController {

    private final HealthPolicyService healthPolicyService;

    /**
     * Obtiene la lista de miembros asociados a una póliza de salud.
     *
     * @param policyId identificador único de la póliza de salud
     * @return lista de miembros de la póliza
     * @throws com.insurance.api.exception.BusinessException si no se encuentra la póliza
     */
    @GetMapping("/{policyId}/members")
    @Operation(summary = "Listar miembros de póliza de salud", description = "Obtiene todos los miembros (titular y familiares) de una póliza de salud")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de miembros obtenida exitosamente"),
        @ApiResponse(responseCode = "400", description = "Póliza no encontrada")
    })
    public List<HealthMember> getMembers(
            @Parameter(description = "ID de la póliza de salud", example = "1")
            @PathVariable Long policyId) {
        return healthPolicyService.getMembers(policyId);
    }

    /**
     * Añade un nuevo miembro a una póliza de salud existente.
     * <p>
     * Valida que la póliza permita añadir miembros adicionales
     * (no sea solo para el titular).
     * </p>
     *
     * @param policyId identificador único de la póliza de salud
     * @param request objeto {@link HealthMemberRequest} con los datos del miembro
     * @return el miembro añadido a la póliza
     * @throws com.insurance.api.exception.BusinessException si la póliza solo cubre al titular
     */
    @PostMapping("/{policyId}/members")
    @Operation(summary = "Añadir miembro a póliza de salud", description = "Añade un familiar a una póliza de salud existente. La póliza debe permitir miembros adicionales.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Miembro añadido exitosamente"),
        @ApiResponse(responseCode = "400", description = "Póliza no encontrada o solo cubre al titular")
    })
    public HealthMember addMember(
            @Parameter(description = "ID de la póliza de salud", example = "1")
            @PathVariable Long policyId,
            @RequestBody HealthMemberRequest request) {

        return healthPolicyService.addMember(policyId, request);
    }

    /**
     * Crea una nueva póliza de salud para un cliente.
     *
     * @param request objeto {@link HealthPolicyRequest} con los datos de la póliza
     * @return la póliza de salud creada
     */
    @PostMapping
    @Operation(summary = "Crear póliza de salud", description = "Crea una nueva póliza de salud para un cliente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Póliza de salud creada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public HealthPolicy create(@RequestBody HealthPolicyRequest request) {
        return healthPolicyService.create(request);
    }

}