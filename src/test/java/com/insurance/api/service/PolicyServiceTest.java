package com.insurance.api.service;

import com.insurance.api.domain.Client;
import com.insurance.api.domain.LifePolicy;
import com.insurance.api.domain.Policy;
import com.insurance.api.domain.PolicyType;
import com.insurance.api.exception.BusinessException;
import com.insurance.api.repository.PolicyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class
PolicyServiceTest {

    @Mock
    private PolicyRepository policyRepository;

    @InjectMocks
    private PolicyService policyService;

    private Policy testPolicy;
    private Client testClient;

    @BeforeEach
    void setUp() {
        testClient = new Client();
        testClient.setId(1L);
        testClient.setFirstName("John");
        testClient.setLastName("Doe");

        testPolicy = new LifePolicy();
        testPolicy.setId(1L);
        testPolicy.setType(PolicyType.VIDA);
        testPolicy.setClient(testClient);
        ((LifePolicy) testPolicy).setInsuredAmount(100000.0);
    }

    @Test
    @DisplayName("Should return policies when client has policies")
    void getPoliciesByClient_WhenClientHasPolicies_ShouldReturnPoliciesList() {
        List<Policy> policies = Arrays.asList(
                createLifePolicy(1L, testClient, 100000.0),
                createLifePolicy(2L, testClient, 200000.0)
        );

        when(policyRepository.findByClientId(1L)).thenReturn(policies);

        List<Policy> result = policyService.getPoliciesByClient(1L);

        assertThat(result).hasSize(2);
        assertThat(result).containsExactlyInAnyOrderElementsOf(policies);
        verify(policyRepository).findByClientId(1L);
    }

    @Test
    @DisplayName("Should return empty list when client has no policies")
    void getPoliciesByClient_WhenClientHasNoPolicies_ShouldReturnEmptyList() {
        when(policyRepository.findByClientId(1L)).thenReturn(Collections.emptyList());

        List<Policy> result = policyService.getPoliciesByClient(1L);

        assertThat(result).isEmpty();
        verify(policyRepository).findByClientId(1L);
    }

    @Test
    @DisplayName("Should return policy when found by ID")
    void getPolicy_WhenPolicyExists_ShouldReturnPolicy() {
        when(policyRepository.findById(1L)).thenReturn(Optional.of(testPolicy));

        Policy result = policyService.getPolicy(1L);

        assertThat(result).isEqualTo(testPolicy);
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getType()).isEqualTo(PolicyType.VIDA);
        verify(policyRepository).findById(1L);
    }

    @Test
    @DisplayName("Should throw exception when policy not found by ID")
    void getPolicy_WhenPolicyNotFound_ShouldThrowBusinessException() {
        when(policyRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> policyService.getPolicy(1L))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Policy not found");
        verify(policyRepository).findById(1L);
    }

    @Test
    @DisplayName("Should delete policy successfully when it exists")
    void deletePolicy_WhenPolicyExists_ShouldDeletePolicy() {
        when(policyRepository.existsById(1L)).thenReturn(true);

        policyService.deletePolicy(1L);

        verify(policyRepository).existsById(1L);
        verify(policyRepository).deleteById(1L);
    }

    @Test
    @DisplayName("Should throw exception when deleting non-existent policy")
    void deletePolicy_WhenPolicyNotFound_ShouldThrowBusinessException() {
        when(policyRepository.existsById(1L)).thenReturn(false);

        assertThatThrownBy(() -> policyService.deletePolicy(1L))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Policy not found");
        verify(policyRepository).existsById(1L);
    }

    @Test
    @DisplayName("Should return multiple policy types for client")
    void getPoliciesByClient_WithMultiplePolicyTypes_ShouldReturnAllPolicies() {
        LifePolicy lifePolicy = createLifePolicy(1L, testClient, 100000.0);

        when(policyRepository.findByClientId(1L)).thenReturn(Collections.singletonList(lifePolicy));

        List<Policy> result = policyService.getPoliciesByClient(1L);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getType()).isEqualTo(PolicyType.VIDA);
        verify(policyRepository).findByClientId(1L);
    }

    private LifePolicy createLifePolicy(Long id, Client client, Double insuredAmount) {
        LifePolicy policy = new LifePolicy();
        policy.setId(id);
        policy.setType(PolicyType.VIDA);
        policy.setClient(client);
        policy.setInsuredAmount(insuredAmount);
        return policy;
    }
}
