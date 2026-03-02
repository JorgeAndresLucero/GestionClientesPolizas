package com.insurance.api.service;

import com.insurance.api.domain.Client;
import com.insurance.api.exception.BusinessException;
import com.insurance.api.repository.ClientRepository;
import com.insurance.api.repository.PolicyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository repository;
    private final PolicyRepository policyRepository;

    public Client create(Client client) {
        return repository.save(client);
    }

    public List<Client> findAll() {
        return repository.findAll();
    }

    public Client findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client not found"));
    }

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