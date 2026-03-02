package com.insurance.api.controller;

import com.insurance.api.domain.Client;
import com.insurance.api.service.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de clientes del sistema de seguros.
 * <p>
 * Proporciona endpoints para crear, consultar, actualizar y eliminar clientes.
 * </p>
 *
 * @author Insurance API Team
 * @version 1.0.0
 * @see ClientService
 * @see Client
 */
@RestController
@RequestMapping("/clients")
@RequiredArgsConstructor
@Tag(name = "Clientes", description = "API para la gestión de clientes del sistema de seguros")
public class ClientController {

    private final ClientService clientService;

    /**
     * Crea un nuevo cliente en el sistema.
     *
     * @param client objeto {@link Client} con los datos del cliente a crear
     * @return el cliente creado con su ID asignado
     */
    @PostMapping
    @Operation(summary = "Crear cliente", description = "Registra un nuevo cliente en el sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cliente creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos")
    })
    public Client create(@RequestBody Client client) {
        return clientService.create(client);
    }

    /**
     * Obtiene la lista completa de clientes registrados en el sistema.
     *
     * @return lista de todos los clientes
     */
    @GetMapping
    @Operation(summary = "Listar clientes", description = "Obtiene todos los clientes registrados")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de clientes obtenida exitosamente")
    })
    public List<Client> findAll() {
        return clientService.findAll();
    }

    /**
     * Busca un cliente por su identificador único.
     *
     * @param id identificador único del cliente
     * @return el cliente encontrado
     * @throws RuntimeException si no se encuentra el cliente
     */
    @GetMapping("/{id}")
    @Operation(summary = "Buscar cliente por ID", description = "Obtiene los detalles de un cliente por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cliente encontrado"),
        @ApiResponse(responseCode = "400", description = "Cliente no encontrado")
    })
    public Client findById(
            @Parameter(description = "ID del cliente", example = "1")
            @PathVariable Long id) {
        return clientService.findById(id);
    }

    /**
     * Actualiza los datos de un cliente existente.
     *
     * @param id identificador único del cliente a actualizar
     * @param request objeto {@link Client} con los nuevos datos del cliente
     * @return el cliente actualizado
     * @throws com.insurance.api.exception.BusinessException si no se encuentra el cliente
     */
    @PutMapping("/{id}")
    @Operation(summary = "Actualizar cliente", description = "Actualiza los datos de un cliente existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cliente actualizado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Cliente no encontrado o datos inválidos")
    })
    public Client updateClient(
            @Parameter(description = "ID del cliente", example = "1")
            @PathVariable Long id,
            @RequestBody Client request) {
        return clientService.updateClient(id, request);
    }

    /**
     * Elimina un cliente del sistema.
     * <p>
     * Solo se puede eliminar si el cliente no tiene pólizas activas.
     * </p>
     *
     * @param id identificador único del cliente a eliminar
     * @return el cliente eliminado
     * @throws com.insurance.api.exception.BusinessException si el cliente tiene pólizas activas
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar cliente", description = "Elimina un cliente del sistema (solo si no tiene pólizas activas)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cliente eliminado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Cliente no encontrado o tiene pólizas activas")
    })
    public Client deleteClient(
            @Parameter(description = "ID del cliente", example = "1")
            @PathVariable Long id) {
        return clientService.deleteClient(id);
    }
}