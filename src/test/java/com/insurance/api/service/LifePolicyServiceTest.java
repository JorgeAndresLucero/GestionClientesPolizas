package com.insurance.api.service;

import com.insurance.api.domain.Client;
import com.insurance.api.domain.LifePolicy;
import com.insurance.api.domain.PolicyType;
import com.insurance.api.dto.LifePolicyRequest;
import com.insurance.api.exception.BusinessException;
import com.insurance.api.repository.LifePolicyRepository;
import com.insurance.api.repository.PolicyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LifePolicyServiceTest {

    @Mock
    private LifePolicyRepository lifePolicyRepository;

    @Mock
    private PolicyRepository policyRepository;

    @InjectMocks
    private LifePolicyService lifePolicyService;

    private LifePolicyRequest request;
    private LifePolicy testPolicy;

    @BeforeEach
    void setUp() {
        request = new LifePolicyRequest();
        request.setClientId(1L);
        request.setInsuredAmount(100000.0);

        testPolicy = new LifePolicy();
        testPolicy.setId(1L);
        testPolicy.setType(PolicyType.VIDA);
        testPolicy.setInsuredAmount(100000.0);

        Client client = new Client();
        client.setId(1L);
        testPolicy.setClient(client);
    }

    @Test
    @DisplayName("Should create life policy successfully when client has no existing policy")
    void createLifePolicy_WhenClientHasNoExistingPolicy_ShouldReturnSavedPolicy() {
        when(policyRepository.existsByClientIdAndType(1L, PolicyType.VIDA)).thenReturn(false);
        when(lifePolicyRepository.save(any(LifePolicy.class))).thenReturn(testPolicy);

        LifePolicy result = lifePolicyService.createLifePolicy(request);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getType()).isEqualTo(PolicyType.VIDA);
        assertThat(result.getInsuredAmount()).isEqualTo(100000.0);
        verify(policyRepository).existsByClientIdAndType(1L, PolicyType.VIDA);
        verify(lifePolicyRepository).save(any(LifePolicy.class));
    }

    @Test
    @DisplayName("Should throw exception when client already has a life policy")
    void createLifePolicy_WhenClientAlreadyHasPolicy_ShouldThrowBusinessException() {
        when(policyRepository.existsByClientIdAndType(1L, PolicyType.VIDA)).thenReturn(true);

        assertThatThrownBy(() -> lifePolicyService.createLifePolicy(request))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Client already has a life policy");
        verify(policyRepository).existsByClientIdAndType(1L, PolicyType.VIDA);
    }

    @Test
    @DisplayName("Should create life policy with different insured amount")
    void createLifePolicy_WithDifferentInsuredAmount_ShouldReturnSavedPolicy() {
        LifePolicyRequest customRequest = new LifePolicyRequest();
        customRequest.setClientId(2L);
        customRequest.setInsuredAmount(250000.0);

        LifePolicy savedPolicy = new LifePolicy();
        savedPolicy.setId(2L);
        savedPolicy.setType(PolicyType.VIDA);
        savedPolicy.setInsuredAmount(250000.0);

        Client client = new Client();
        client.setId(2L);
        savedPolicy.setClient(client);

        when(policyRepository.existsByClientIdAndType(2L, PolicyType.VIDA)).thenReturn(false);
        when(lifePolicyRepository.save(any(LifePolicy.class))).thenReturn(savedPolicy);

        LifePolicy result = lifePolicyService.createLifePolicy(customRequest);

        assertThat(result.getInsuredAmount()).isEqualTo(250000.0);
        verify(lifePolicyRepository).save(any(LifePolicy.class));
    }

    @Test
    @DisplayName("Should verify policy type is VIDA when checking existence")
    void createLifePolicy_ShouldCheckForVIDAType() {
        when(policyRepository.existsByClientIdAndType(1L, PolicyType.VIDA)).thenReturn(false);
        when(lifePolicyRepository.save(any(LifePolicy.class))).thenReturn(testPolicy);

        lifePolicyService.createLifePolicy(request);

        verify(policyRepository).existsByClientIdAndType(1L, PolicyType.VIDA);
    }

    @Test
    @DisplayName("Should set client id correctly on policy")
    void createLifePolicy_ShouldSetClientIdOnPolicy() {
        when(policyRepository.existsByClientIdAndType(1L, PolicyType.VIDA)).thenReturn(false);
        when(lifePolicyRepository.save(any(LifePolicy.class))).thenAnswer(invocation -> {
            LifePolicy policy = invocation.getArgument(0);
            return policy;
        });

        LifePolicy result = lifePolicyService.createLifePolicy(request);

        assertThat(result.getClient().getId()).isEqualTo(1L);
    }
}
