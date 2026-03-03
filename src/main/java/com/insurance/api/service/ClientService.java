package com.insurance.api.service;

import com.insurance.api.domain.Client;
import com.insurance.api.exception.BusinessException;
import com.insurance.api.repository.ClientRepository;
import com.insurance.api.repository.PolicyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Servicio para la gestión de operaciones de negocio relacionadas con clientes.
 * <p>
 * Proporciona funcionalidad para crear, consultar, actualizar y eliminar clientes,
 * así como validaciones relacionadas con sus pólizas asociadas.
 * </p>
 *
 * @author Insurance API Team
 * @version 1.0.0
 * @see Client
 * @see ClientRepository
 * @see PolicyRepository
 */
@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository repository;
    private final PolicyRepository policyRepository;

    /**
     * Crea un nuevo cliente en el sistema.
     *
     * @param client objeto {@link Client} con los datos del cliente a crear
     * @return el cliente creado con su ID asignado
     */
    public Client create(Client client) {
        return repository.save(client);
    }

    /**
     * Obtiene la lista completa de todos los clientes registrados.
     *
     * @return lista de todos los clientes
     */
    public List<Client> findAll() {
        return repository.findAll();
    }

    /**
     * Busca un cliente por su identificador único.
     *
     * @param id identificador único del cliente
     * @return el cliente encontrado
     * @throws RuntimeException si no se encuentra el cliente
     */
    public Client findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client not found"));
    }

    /**
     * Actualiza los datos de un cliente existente.
     * <p>
     * Actualiza todos los campos del cliente: tipo y número de documento,
     * nombre, apellido, email, teléfono y fecha de nacimiento.
     * </p>
     *
     * @param id identificador único del cliente a actualizar
     * @param request objeto {@link Client} con los nuevos datos del cliente
     * @return el cliente actualizado
     * @throws BusinessException si no se encuentra el cliente
     */
    public Client updateClient(Long id, Client request) {

        Client client = repository.findById(id)
                .orElseThrow(() -> new BusinessException("Client not found"));

        client.setDocumentType(request.getDocumentType());
        client.setDocumentNumber(request.getDocumentNumber());
        client.setFirstName(request.getFirstName());
        client.setLastName(request.getLastName());
        client.setEmail(request.getEmail());
        client.setPhone(request.getPhone());
        client.setBirthDate(request.getBirthDate());

        return repository.save(client);
    }

    /**
     * Elimina un cliente del sistema.
     * <p>
     * Valida que el cliente no tenga pólizas activas antes de eliminarlo.
     * </p>
     *
     * @param id identificador único del cliente a eliminar
     * @return el cliente eliminado
     * @throws BusinessException si el cliente tiene pólizas activas
     */
    public Client deleteClient(Long id) {

        Client client = repository.findById(id)
                .orElseThrow(() -> new BusinessException("Client not found"));

        boolean hasPolicies = policyRepository.existsByClientId(id);

        if (hasPolicies) {
            throw new BusinessException("Client has active policies");
        }

        repository.delete(client);
        return client;
    }
}