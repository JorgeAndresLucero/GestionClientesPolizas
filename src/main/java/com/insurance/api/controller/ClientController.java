package com.insurance.api.controller;

import com.insurance.api.domain.Client;
import com.insurance.api.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @PostMapping
    public Client create(@RequestBody Client client) {
        return clientService.create(client);
    }

    @GetMapping
    public List<Client> findAll() {
        return clientService.findAll();
    }

    @GetMapping("/{id}")
    public Client findById(@PathVariable Long id) {
        return clientService.findById(id);
    }

    @PutMapping("/{id}")
    public Client updateClient(@PathVariable Long id,
                               @RequestBody Client request) {
        return clientService.updateClient(id, request);
    }

    @DeleteMapping("/{id}")
    public Client deleteClient(@PathVariable Long id) {
        return clientService.deleteClient(id);
    }
}