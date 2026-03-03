package com.insurance.api.service;

import com.insurance.api.domain.Beneficiary;
import com.insurance.api.domain.LifePolicy;
import com.insurance.api.dto.BeneficiaryRequest;
import com.insurance.api.exception.BusinessException;
import com.insurance.api.repository.BeneficiaryRepository;
import com.insurance.api.repository.LifePolicyRepository;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BeneficiaryServiceTest {

    @Mock
    private BeneficiaryRepository beneficiaryRepository;

    @Mock
    private LifePolicyRepository lifePolicyRepository;

    @InjectMocks
    private BeneficiaryService beneficiaryService;

    private LifePolicy testPolicy;
    private BeneficiaryRequest testRequest;
    private Beneficiary testBeneficiary;

    @BeforeEach
    void setUp() {
        testPolicy = new LifePolicy();
        testPolicy.setId(1L);
        testPolicy.setBeneficiaries(new java.util.ArrayList<>());
        testPolicy.setInsuredAmount(100000.0);

        testRequest = new BeneficiaryRequest();
        testRequest.setName("John Doe");
        testRequest.setRelationship("Spouse");

        testBeneficiary = Beneficiary.builder()
                .id(1L)
                .name("John Doe")
                .relationship("Spouse")
                .lifePolicy(testPolicy)
                .build();
    }

    @Test
    @DisplayName("Should add beneficiary to policy successfully")
    void addBeneficiary_WhenPolicyExistsAndHasLessThan2Beneficiaries_ShouldReturnSavedBeneficiary() {
        when(lifePolicyRepository.findById(1L)).thenReturn(Optional.of(testPolicy));
        when(beneficiaryRepository.save(any(Beneficiary.class))).thenReturn(testBeneficiary);

        Beneficiary result = beneficiaryService.addBeneficiary(1L, testRequest);

        assertThat(result.getName()).isEqualTo("John Doe");
        assertThat(result.getRelationship()).isEqualTo("Spouse");
        assertThat(result.getLifePolicy()).isEqualTo(testPolicy);
        verify(lifePolicyRepository).findById(1L);
        verify(beneficiaryRepository).save(any(Beneficiary.class));
    }

    @Test
    @DisplayName("Should throw exception when policy not found")
    void addBeneficiary_WhenPolicyNotFound_ShouldThrowBusinessException() {
        when(lifePolicyRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> beneficiaryService.addBeneficiary(1L, testRequest))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Life policy not found");
        verify(lifePolicyRepository).findById(1L);
    }

    @Test
    @DisplayName("Should throw exception when policy already has 2 beneficiaries")
    void addBeneficiary_WhenPolicyHas2Beneficiaries_ShouldThrowBusinessException() {
        testPolicy.setBeneficiaries(Arrays.asList(
                Beneficiary.builder().id(1L).name("Beneficiary 1").relationship("Spouse").build(),
                Beneficiary.builder().id(2L).name("Beneficiary 2").relationship("Child").build()
        ));

        when(lifePolicyRepository.findById(1L)).thenReturn(Optional.of(testPolicy));

        assertThatThrownBy(() -> beneficiaryService.addBeneficiary(1L, testRequest))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Life policy can only have 2 beneficiaries");
        verify(lifePolicyRepository).findById(1L);
    }

    @Test
    @DisplayName("Should return beneficiaries when policy has beneficiaries")
    void getByPolicy_WhenPolicyHasBeneficiaries_ShouldReturnBeneficiariesList() {
        List<Beneficiary> beneficiaries = Arrays.asList(
                Beneficiary.builder().id(1L).name("John Doe").relationship("Spouse").lifePolicy(testPolicy).build(),
                Beneficiary.builder().id(2L).name("Jane Doe").relationship("Child").lifePolicy(testPolicy).build()
        );

        when(beneficiaryRepository.findByLifePolicyId(1L)).thenReturn(beneficiaries);

        List<Beneficiary> result = beneficiaryService.getByPolicy(1L);

        assertThat(result).hasSize(2);
        assertThat(result).containsExactlyInAnyOrderElementsOf(beneficiaries);
        verify(beneficiaryRepository).findByLifePolicyId(1L);
    }

    @Test
    @DisplayName("Should throw exception when policy has no beneficiaries")
    void getByPolicy_WhenPolicyHasNoBeneficiaries_ShouldThrowBusinessException() {
        when(beneficiaryRepository.findByLifePolicyId(1L)).thenReturn(Collections.emptyList());

        assertThatThrownBy(() -> beneficiaryService.getByPolicy(1L))
                .isInstanceOf(BusinessException.class)
                .hasMessage("No beneficiaries found for this policy");
        verify(beneficiaryRepository).findByLifePolicyId(1L);
    }

    @Test
    @DisplayName("Should return all beneficiaries")
    void findAll_ShouldReturnListOfAllBeneficiaries() {
        List<Beneficiary> beneficiaries = Arrays.asList(
                Beneficiary.builder().id(1L).name("John Doe").relationship("Spouse").build(),
                Beneficiary.builder().id(2L).name("Jane Doe").relationship("Child").build(),
                Beneficiary.builder().id(3L).name("Bob Smith").relationship("Parent").build()
        );

        when(beneficiaryRepository.findAll()).thenReturn(beneficiaries);

        List<Beneficiary> result = beneficiaryService.findAll();

        assertThat(result).hasSize(3);
        assertThat(result).containsExactlyInAnyOrderElementsOf(beneficiaries);
        verify(beneficiaryRepository).findAll();
    }

    @Test
    @DisplayName("Should return empty list when no beneficiaries exist")
    void findAll_WhenNoBeneficiariesExist_ShouldReturnEmptyList() {
        when(beneficiaryRepository.findAll()).thenReturn(Collections.emptyList());

        List<Beneficiary> result = beneficiaryService.findAll();

        assertThat(result).isEmpty();
        verify(beneficiaryRepository).findAll();
    }
}
