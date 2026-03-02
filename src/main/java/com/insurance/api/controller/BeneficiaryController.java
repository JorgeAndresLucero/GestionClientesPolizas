package com.insurance.api.controller;

import com.insurance.api.domain.Beneficiary;
import com.insurance.api.dto.BeneficiaryRequest;
import com.insurance.api.service.BeneficiaryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de beneficiarios de pólizas de vida.
 * <p>
 * Proporciona endpoints para consultar y añadir beneficiarios a las pólizas de vida.
 * Cada póliza puede tener un máximo de 2 beneficiarios.
 * </p>
 *
 * @author Insurance API Team
 * @version 1.0.0
 * @see BeneficiaryService
 * @see Beneficiary
 */
@RestController
@RequestMapping("/policies")
@RequiredArgsConstructor
@Tag(name = "Beneficiarios", description = "API para la gestión de beneficiarios de pólizas de vida")
public class BeneficiaryController {

    private final BeneficiaryService beneficiaryService;

    /**
     * Obtiene la lista de beneficiarios de una póliza de vida específica.
     *
     * @param policyId identificador único de la póliza de vida
     * @return lista de beneficiarios de la póliza
     * @throws com.insurance.api.exception.BusinessException si no hay beneficiarios para la póliza
     */
    @GetMapping("/life/{policyId}/beneficiaries")
    @Operation(summary = "Listar beneficiarios de póliza de vida", description = "Obtiene los beneficiarios de una póliza de vida específica (máximo 2)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de beneficiarios obtenida exitosamente"),
        @ApiResponse(responseCode = "400", description = "No hay beneficiarios para esta póliza")
    })
    public List<Beneficiary> getBeneficiaries(
            @Parameter(description = "ID de la póliza de vida", example = "1")
            @PathVariable Long policyId) {
        return beneficiaryService.getByPolicy(policyId);
    }

    /**
     * Obtiene la lista completa de todos los beneficiarios del sistema.
     *
     * @return lista de todos los beneficiarios registrados
     */
    @GetMapping("/beneficiaries")
    @Operation(summary = "Listar todos los beneficiarios", description = "Obtiene todos los beneficiarios registrados en el sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de beneficiarios obtenida exitosamente")
    })
    public List<Beneficiary> findAll() {
        return beneficiaryService.findAll();
    }

    /**
     * Añade un nuevo beneficiario a una póliza de vida.
     * <p>
     * Valida que la póliza no haya alcanzado el límite máximo de 2 beneficiarios.
     * </p>
     *
     * @param policyId identificador único de la póliza de vida
     * @param request objeto {@link BeneficiaryRequest} con los datos del beneficiario
     * @return el beneficiario añadido
     * @throws com.insurance.api.exception.BusinessException si la póliza ya tiene 2 beneficiarios
     */
    @PostMapping("/life/{policyId}/beneficiaries")
    @Operation(summary = "Añadir beneficiario a póliza de vida", description = "Añade un beneficiario a una póliza de vida. Máximo 2 beneficiarios por póliza.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Beneficiario añadido exitosamente"),
        @ApiResponse(responseCode = "400", description = "Póliza no encontrada o ya tiene 2 beneficiarios")
    })
    public Beneficiary addBeneficiary(
            @Parameter(description = "ID de la póliza de vida", example = "1")
            @PathVariable Long policyId,
            @RequestBody BeneficiaryRequest request) {

        return beneficiaryService.addBeneficiary(policyId, request);
    }

}
