package com.insurance.api.controller;

import com.insurance.api.domain.LifePolicy;
import com.insurance.api.dto.LifePolicyRequest;
import com.insurance.api.service.LifePolicyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador REST para la gestión de pólizas de vida.
 * <p>
 * Proporciona endpoints para crear nuevas pólizas de vida para los clientes.
 * Cada cliente solo puede tener una póliza de vida activa.
 * </p>
 *
 * @author Insurance API Team
 * @version 1.0.0
 * @see LifePolicyService
 * @see LifePolicy
 */
@RestController
@RequestMapping("/policies/life")
@RequiredArgsConstructor
@Tag(name = "Pólizas de Vida", description = "API para la gestión de pólizas de seguro de vida")
public class LifePolicyController {

    private final LifePolicyService lifePolicyService;

    /**
     * Crea una nueva póliza de vida para un cliente.
     * <p>
     * Valida que el cliente no tenga ya una póliza de vida activa.
     * </p>
     *
     * @param request objeto {@link LifePolicyRequest} con los datos de la póliza
     * @return la póliza de vida creada
     * @throws com.insurance.api.exception.BusinessException si el cliente ya tiene una póliza de vida
     */
    @PostMapping
    @Operation(summary = "Crear póliza de vida", description = "Crea una nueva póliza de vida para un cliente. Cada cliente solo puede tener una póliza de vida.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Póliza de vida creada exitosamente"),
        @ApiResponse(responseCode = "400", description = "El cliente ya tiene una póliza de vida o datos inválidos")
    })
    public LifePolicy create(@RequestBody LifePolicyRequest request) {
        return lifePolicyService.createLifePolicy(request);
    }
}