package com.insurance.api.service;

import com.insurance.api.domain.Client;
import com.insurance.api.domain.HealthMember;
import com.insurance.api.domain.HealthPolicy;
import com.insurance.api.domain.PolicyType;
import com.insurance.api.dto.HealthMemberRequest;
import com.insurance.api.dto.HealthPolicyRequest;
import com.insurance.api.exception.BusinessException;
import com.insurance.api.repository.HealthMemberRepository;
import com.insurance.api.repository.HealthPolicyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HealthServiceTest {

    @Mock
    private HealthPolicyRepository healthPolicyRepository;

    @Mock
    private HealthMemberRepository healthMemberRepository;

    @InjectMocks
    private HealthService healthService;

    private HealthPolicy testPolicy;
    private HealthMemberRequest memberRequest;
    private HealthPolicyRequest policyRequest;

    @BeforeEach
    void setUp() {
        testPolicy = new HealthPolicy();
        testPolicy.setId(1L);
        testPolicy.setType(PolicyType.SALUD);
        testPolicy.setCoversClientOnly(false);
        testPolicy.setMembers(new ArrayList<>());

        Client client = new Client();
        client.setId(1L);
        testPolicy.setClient(client);

        memberRequest = new HealthMemberRequest();
        memberRequest.setName("John Doe");
        memberRequest.setRelationship("ESPOSA");

        policyRequest = new HealthPolicyRequest();
        policyRequest.setClientId(1L);
        policyRequest.setCoversClientOnly(false);
    }

    @Test
    @DisplayName("Should create health policy successfully")
    void createHealthPolicy_ShouldReturnSavedPolicy() {
        HealthPolicy savedPolicy = new HealthPolicy();
        savedPolicy.setId(1L);
        savedPolicy.setType(PolicyType.SALUD);
        savedPolicy.setCoversClientOnly(false);

        Client client = new Client();
        client.setId(1L);
        savedPolicy.setClient(client);

        when(healthPolicyRepository.save(any(HealthPolicy.class))).thenReturn(savedPolicy);

        HealthPolicy result = healthService.createHealthPolicy(policyRequest);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getType()).isEqualTo(PolicyType.SALUD);
        assertThat(result.getCoversClientOnly()).isFalse();
        verify(healthPolicyRepository).save(any(HealthPolicy.class));
    }

    @Test
    @DisplayName("Should create health policy with coversClientOnly true")
    void createHealthPolicy_WithCoversClientOnlyTrue_ShouldReturnSavedPolicy() {
        HealthPolicyRequest request = new HealthPolicyRequest();
        request.setClientId(1L);
        request.setCoversClientOnly(true);

        HealthPolicy savedPolicy = new HealthPolicy();
        savedPolicy.setId(1L);
        savedPolicy.setType(PolicyType.SALUD);
        savedPolicy.setCoversClientOnly(true);

        Client client = new Client();
        client.setId(1L);
        savedPolicy.setClient(client);

        when(healthPolicyRepository.save(any(HealthPolicy.class))).thenReturn(savedPolicy);

        HealthPolicy result = healthService.createHealthPolicy(request);

        assertThat(result.getCoversClientOnly()).isTrue();
        verify(healthPolicyRepository).save(any(HealthPolicy.class));
    }

    @Test
    @DisplayName("Should add member to policy successfully")
    void addMember_WhenPolicyExistsAndNotClientOnly_ShouldReturnSavedMember() {
        HealthMember testMember = HealthMember.builder()
                .id(1L)
                .name("John Doe")
                .relationship("ESPOSA")
                .healthPolicy(testPolicy)
                .build();

        when(healthPolicyRepository.findById(1L)).thenReturn(Optional.of(testPolicy));
        when(healthMemberRepository.save(any(HealthMember.class))).thenReturn(testMember);

        HealthMember result = healthService.addMember(1L, memberRequest);

        assertThat(result.getName()).isEqualTo("John Doe");
        assertThat(result.getRelationship()).isEqualTo("ESPOSA");
        assertThat(result.getHealthPolicy()).isEqualTo(testPolicy);
        verify(healthPolicyRepository).findById(1L);
        verify(healthMemberRepository).save(any(HealthMember.class));
    }

    @Test
    @DisplayName("Should throw exception when policy not found")
    void addMember_WhenPolicyNotFound_ShouldThrowBusinessException() {
        when(healthPolicyRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> healthService.addMember(1L, memberRequest))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Health policy not found");
        verify(healthPolicyRepository).findById(1L);
    }

    @Test
    @DisplayName("Should throw exception when policy covers client only")
    void addMember_WhenPolicyCoversClientOnly_ShouldThrowBusinessException() {
        testPolicy.setCoversClientOnly(true);

        when(healthPolicyRepository.findById(1L)).thenReturn(Optional.of(testPolicy));

        assertThatThrownBy(() -> healthService.addMember(1L, memberRequest))
                .isInstanceOf(BusinessException.class)
                .hasMessage("This policy only covers the client");
        verify(healthPolicyRepository).findById(1L);
    }

    @Test
    @DisplayName("Should throw exception when adding third parent")
    void addMember_WhenAlreadyTwoParents_ShouldThrowBusinessException() {
        testPolicy.setMembers(Arrays.asList(
                HealthMember.builder().id(1L).name("Father").relationship("PADRE").build(),
                HealthMember.builder().id(2L).name("Mother").relationship("MADRE").build()
        ));

        HealthMemberRequest parentRequest = new HealthMemberRequest();
        parentRequest.setName("Another Parent");
        parentRequest.setRelationship("PADRE");

        when(healthPolicyRepository.findById(1L)).thenReturn(Optional.of(testPolicy));

        assertThatThrownBy(() -> healthService.addMember(1L, parentRequest))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Only 2 parents allowed");
        verify(healthPolicyRepository).findById(1L);
    }

    @Test
    @DisplayName("Should allow adding second parent")
    void addMember_WhenOnlyOneParent_ShouldAllowSecondParent() {
        testPolicy.setMembers(Arrays.asList(
                HealthMember.builder().id(1L).name("Father").relationship("PADRE").build()
        ));

        HealthMemberRequest motherRequest = new HealthMemberRequest();
        motherRequest.setName("Mother");
        motherRequest.setRelationship("MADRE");

        HealthMember savedMember = HealthMember.builder()
                .id(2L)
                .name("Mother")
                .relationship("MADRE")
                .healthPolicy(testPolicy)
                .build();

        when(healthPolicyRepository.findById(1L)).thenReturn(Optional.of(testPolicy));
        when(healthMemberRepository.save(any(HealthMember.class))).thenReturn(savedMember);

        HealthMember result = healthService.addMember(1L, motherRequest);

        assertThat(result.getName()).isEqualTo("Mother");
        assertThat(result.getRelationship()).isEqualTo("MADRE");
        verify(healthPolicyRepository).findById(1L);
        verify(healthMemberRepository).save(any(HealthMember.class));
    }

    @Test
    @DisplayName("Should throw exception when adding second spouse")
    void addMember_WhenSpouseExists_ShouldThrowBusinessException() {
        testPolicy.setMembers(Arrays.asList(
                HealthMember.builder().id(1L).name("Spouse 1").relationship("ESPOSA").build()
        ));

        HealthMemberRequest spouseRequest = new HealthMemberRequest();
        spouseRequest.setName("Spouse 2");
        spouseRequest.setRelationship("ESPOSA");

        when(healthPolicyRepository.findById(1L)).thenReturn(Optional.of(testPolicy));

        assertThatThrownBy(() -> healthService.addMember(1L, spouseRequest))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Only one spouse allowed");
        verify(healthPolicyRepository).findById(1L);
    }

    @Test
    @DisplayName("Should allow adding child when no restrictions")
    void addMember_WhenAddingChild_ShouldSucceed() {
        testPolicy.setMembers(new ArrayList<>());

        HealthMemberRequest childRequest = new HealthMemberRequest();
        childRequest.setName("Child");
        childRequest.setRelationship("HIJO");

        HealthMember savedMember = HealthMember.builder()
                .id(1L)
                .name("Child")
                .relationship("HIJO")
                .healthPolicy(testPolicy)
                .build();

        when(healthPolicyRepository.findById(1L)).thenReturn(Optional.of(testPolicy));
        when(healthMemberRepository.save(any(HealthMember.class))).thenReturn(savedMember);

        HealthMember result = healthService.addMember(1L, childRequest);

        assertThat(result.getName()).isEqualTo("Child");
        assertThat(result.getRelationship()).isEqualTo("HIJO");
        verify(healthPolicyRepository).findById(1L);
        verify(healthMemberRepository).save(any(HealthMember.class));
    }
}
