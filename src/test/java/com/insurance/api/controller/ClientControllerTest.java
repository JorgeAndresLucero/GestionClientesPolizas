package com.insurance.api.controller;

import com.insurance.api.domain.Client;
import com.insurance.api.exception.BusinessException;
import com.insurance.api.service.ClientService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClientController.class)
class ClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClientService clientService;

    @Autowired
    private ObjectMapper objectMapper;

    private Client testClient;

    @BeforeEach
    void setUp() {
        testClient = new Client();
        testClient.setId(1L);
        testClient.setDocumentType("DNI");
        testClient.setDocumentNumber("12345678A");
        testClient.setFirstName("John");
        testClient.setLastName("Doe");
        testClient.setEmail("john.doe@example.com");
        testClient.setPhone("555-1234");
        testClient.setBirthDate(LocalDate.of(1990, 5, 15));
    }

    @Test
    @DisplayName("Should create client successfully")
    void create_ShouldReturnCreatedClient() throws Exception {
        when(clientService.create(any(Client.class))).thenReturn(testClient);

        mockMvc.perform(post("/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testClient)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));
    }

    @Test
    @DisplayName("Should return all clients")
    void findAll_ShouldReturnListOfClients() throws Exception {
        List<Client> clients = Arrays.asList(testClient, createClient(2L, "Jane", "Doe"));

        when(clientService.findAll()).thenReturn(clients);

        mockMvc.perform(get("/clients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[1].firstName").value("Jane"));
    }

    @Test
    @DisplayName("Should return client when found by ID")
    void findById_WhenClientExists_ShouldReturnClient() throws Exception {
        when(clientService.findById(1L)).thenReturn(testClient);

        mockMvc.perform(get("/clients/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.firstName").value("John"));
    }

    @Test
    @DisplayName("Should return 400 when client not found")
    void findById_WhenClientNotFound_ShouldReturnBadRequest() throws Exception {
        when(clientService.findById(1L)).thenThrow(new BusinessException("Client not found"));

        mockMvc.perform(get("/clients/1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should update client successfully")
    void updateClient_WhenClientExists_ShouldReturnUpdatedClient() throws Exception {
        Client updatedClient = createClient(1L, "John Updated", "Doe Updated");
        when(clientService.updateClient(anyLong(), any(Client.class))).thenReturn(updatedClient);

        mockMvc.perform(put("/clients/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedClient)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John Updated"))
                .andExpect(jsonPath("$.lastName").value("Doe Updated"));
    }

    @Test
    @DisplayName("Should return 400 when updating non-existent client")
    void updateClient_WhenClientNotFound_ShouldReturnBadRequest() throws Exception {
        Client request = createClient(1L, "John", "Doe");
        when(clientService.updateClient(anyLong(), any(Client.class)))
                .thenThrow(new BusinessException("Client not found"));

        mockMvc.perform(put("/clients/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should delete client successfully")
    void deleteClient_WhenClientExists_ShouldReturnDeletedClient() throws Exception {
        when(clientService.deleteClient(1L)).thenReturn(testClient);

        mockMvc.perform(delete("/clients/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    @DisplayName("Should return 400 when deleting non-existent client")
    void deleteClient_WhenClientNotFound_ShouldReturnBadRequest() throws Exception {
        when(clientService.deleteClient(1L))
                .thenThrow(new BusinessException("Client not found"));

        mockMvc.perform(delete("/clients/1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 400 when client has active policies")
    void deleteClient_WhenClientHasPolicies_ShouldReturnBadRequest() throws Exception {
        when(clientService.deleteClient(1L))
                .thenThrow(new BusinessException("Client has active policies"));

        mockMvc.perform(delete("/clients/1"))
                .andExpect(status().isBadRequest());
    }

    private Client createClient(Long id, String firstName, String lastName) {
        Client client = new Client();
        client.setId(id);
        client.setDocumentType("DNI");
        client.setDocumentNumber("12345678A");
        client.setFirstName(firstName);
        client.setLastName(lastName);
        client.setEmail(firstName.toLowerCase() + "@example.com");
        client.setPhone("555-1234");
        client.setBirthDate(LocalDate.of(1990, 5, 15));
        return client;
    }
}
