package com.insurance.api.service;

import com.insurance.api.domain.Client;
import com.insurance.api.exception.BusinessException;
import com.insurance.api.repository.ClientRepository;
import com.insurance.api.repository.PolicyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    @Mock
    private ClientRepository repository;

    @Mock
    private PolicyRepository policyRepository;

    @InjectMocks
    private ClientService clientService;

    private Client testClient;

    @BeforeEach
    void setUp() {
        testClient = Client.builder()
                .id(1L)
                .documentType("DNI")
                .documentNumber("12345678A")
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .phone("555-1234")
                .birthDate(LocalDate.of(1990, 5, 15))
                .build();
    }

    @Test
    @DisplayName("Should create a new client successfully")
    void create_ShouldReturnSavedClient() {
        when(repository.save(testClient)).thenReturn(testClient);

        Client result = clientService.create(testClient);

        assertThat(result).isEqualTo(testClient);
        verify(repository).save(testClient);
    }

    @Test
    @DisplayName("Should return all clients")
    void findAll_ShouldReturnListOfClients() {
        List<Client> clients = Arrays.asList(testClient, Client.builder()
                .id(2L)
                .documentType("DNI")
                .documentNumber("87654321B")
                .firstName("Jane")
                .lastName("Doe")
                .email("jane.doe@example.com")
                .phone("555-5678")
                .birthDate(LocalDate.of(1992, 8, 20))
                .build());

        when(repository.findAll()).thenReturn(clients);

        List<Client> result = clientService.findAll();

        assertThat(result).hasSize(2);
        assertThat(result).containsExactlyInAnyOrderElementsOf(clients);
        verify(repository).findAll();
    }

    @Test
    @DisplayName("Should return client when found by ID")
    void findById_WhenClientExists_ShouldReturnClient() {
        when(repository.findById(1L)).thenReturn(Optional.of(testClient));

        Client result = clientService.findById(1L);

        assertThat(result).isEqualTo(testClient);
        verify(repository).findById(1L);
    }

    @Test
    @DisplayName("Should throw exception when client not found by ID")
    void findById_WhenClientNotExists_ShouldThrowRuntimeException() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> clientService.findById(1L))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Client not found");
    }

    @Test
    @DisplayName("Should update client successfully")
    void updateClient_WhenClientExists_ShouldReturnUpdatedClient() {
        Client updateRequest = Client.builder()
                .documentType("NIE")
                .documentNumber("X1234567")
                .firstName("John Updated")
                .lastName("Doe Updated")
                .email("john.updated@example.com")
                .phone("555-9999")
                .birthDate(LocalDate.of(1991, 6, 16))
                .build();

        when(repository.findById(1L)).thenReturn(Optional.of(testClient));
        when(repository.save(any(Client.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Client result = clientService.updateClient(1L, updateRequest);

        assertThat(result.getDocumentType()).isEqualTo("NIE");
        assertThat(result.getDocumentNumber()).isEqualTo("X1234567");
        assertThat(result.getFirstName()).isEqualTo("John Updated");
        assertThat(result.getLastName()).isEqualTo("Doe Updated");
        assertThat(result.getEmail()).isEqualTo("john.updated@example.com");
        assertThat(result.getPhone()).isEqualTo("555-9999");
        assertThat(result.getBirthDate()).isEqualTo(LocalDate.of(1991, 6, 16));
        verify(repository).save(any(Client.class));
    }

    @Test
    @DisplayName("Should throw BusinessException when updating non-existent client")
    void updateClient_WhenClientNotExists_ShouldThrowBusinessException() {
        Client updateRequest = Client.builder()
                .documentType("DNI")
                .documentNumber("12345678A")
                .firstName("John")
                .lastName("Doe")
                .email("john@example.com")
                .phone("555-1234")
                .birthDate(LocalDate.of(1990, 5, 15))
                .build();

        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> clientService.updateClient(1L, updateRequest))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Client not found");
    }

    @Test
    @DisplayName("Should delete client successfully when no policies exist")
    void deleteClient_WhenClientExistsAndNoPolicies_ShouldReturnDeletedClient() {
        when(repository.findById(1L)).thenReturn(Optional.of(testClient));
        when(policyRepository.existsByClientId(1L)).thenReturn(false);

        Client result = clientService.deleteClient(1L);

        assertThat(result).isEqualTo(testClient);
        verify(repository).delete(testClient);
        verify(policyRepository).existsByClientId(1L);
    }

    @Test
    @DisplayName("Should throw BusinessException when deleting client with active policies")
    void deleteClient_WhenClientHasPolicies_ShouldThrowBusinessException() {
        when(repository.findById(1L)).thenReturn(Optional.of(testClient));
        when(policyRepository.existsByClientId(1L)).thenReturn(true);

        assertThatThrownBy(() -> clientService.deleteClient(1L))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Client has active policies");
        verify(repository).findById(1L);
        verify(policyRepository).existsByClientId(1L);
    }

    @Test
    @DisplayName("Should throw BusinessException when deleting non-existent client")
    void deleteClient_WhenClientNotExists_ShouldThrowBusinessException() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> clientService.deleteClient(1L))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Client not found");
    }
}
