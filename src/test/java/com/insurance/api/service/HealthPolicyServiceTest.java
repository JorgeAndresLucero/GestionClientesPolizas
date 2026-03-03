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
class HealthPolicyServiceTest {

    @Mock
    private HealthPolicyRepository repository;

    @Mock
    private HealthMemberRepository memberRepository;

    @InjectMocks
    private HealthPolicyService healthPolicyService;

    private HealthPolicy testPolicy;
    private HealthMember testMember;
    private HealthMemberRequest memberRequest;
    private HealthPolicyRequest policyRequest;

    @BeforeEach
    void setUp() {
        testPolicy = new HealthPolicy();
        testPolicy.setId(1L);
        testPolicy.setType(PolicyType.SALUD);
        testPolicy.setCoversClientOnly(false);
        testPolicy.setMembers(new java.util.ArrayList<>());

        Client client = new Client();
        client.setId(1L);
        testPolicy.setClient(client);

        testMember = HealthMember.builder()
                .id(1L)
                .name("John Doe")
                .relationship("SPOUSE")
                .healthPolicy(testPolicy)
                .build();

        memberRequest = new HealthMemberRequest();
        memberRequest.setName("John Doe");
        memberRequest.setRelationship("SPOUSE");

        policyRequest = new HealthPolicyRequest();
        policyRequest.setClientId(1L);
        policyRequest.setCoversClientOnly(false);
    }

    @Test
    @DisplayName("Should return members when policy exists")
    void getMembers_WhenPolicyExists_ShouldReturnMembersList() {
        List<HealthMember> members = Arrays.asList(
                HealthMember.builder().id(1L).name("John Doe").relationship("SPOUSE").build(),
                HealthMember.builder().id(2L).name("Jane Doe").relationship("CHILD").build()
        );

        when(repository.findById(1L)).thenReturn(Optional.of(testPolicy));
        when(memberRepository.findByHealthPolicyId(1L)).thenReturn(members);

        List<HealthMember> result = healthPolicyService.getMembers(1L);

        assertThat(result).hasSize(2);
        assertThat(result).containsExactlyInAnyOrderElementsOf(members);
        verify(repository).findById(1L);
        verify(memberRepository).findByHealthPolicyId(1L);
    }

    @Test
    @DisplayName("Should throw exception when policy not found in getMembers")
    void getMembers_WhenPolicyNotFound_ShouldThrowBusinessException() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> healthPolicyService.getMembers(1L))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Health policy not found");
        verify(repository).findById(1L);
    }

    @Test
    @DisplayName("Should add member to policy successfully")
    void addMember_WhenPolicyExistsAndNotClientOnly_ShouldReturnSavedMember() {
        when(repository.findById(1L)).thenReturn(Optional.of(testPolicy));
        when(memberRepository.save(any(HealthMember.class))).thenReturn(testMember);

        HealthMember result = healthPolicyService.addMember(1L, memberRequest);

        assertThat(result.getName()).isEqualTo("John Doe");
        assertThat(result.getRelationship()).isEqualTo("SPOUSE");
        assertThat(result.getHealthPolicy()).isEqualTo(testPolicy);
        verify(repository).findById(1L);
        verify(memberRepository).save(any(HealthMember.class));
    }

    @Test
    @DisplayName("Should throw exception when policy not found in addMember")
    void addMember_WhenPolicyNotFound_ShouldThrowBusinessException() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> healthPolicyService.addMember(1L, memberRequest))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Health policy not found");
        verify(repository).findById(1L);
    }

    @Test
    @DisplayName("Should throw exception when policy covers client only")
    void addMember_WhenPolicyCoversClientOnly_ShouldThrowBusinessException() {
        testPolicy.setCoversClientOnly(true);

        when(repository.findById(1L)).thenReturn(Optional.of(testPolicy));

        assertThatThrownBy(() -> healthPolicyService.addMember(1L, memberRequest))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Policy only covers the client");
        verify(repository).findById(1L);
    }

    @Test
    @DisplayName("Should create health policy successfully")
    void create_ShouldReturnSavedPolicy() {
        HealthPolicy savedPolicy = new HealthPolicy();
        savedPolicy.setId(1L);
        savedPolicy.setType(PolicyType.SALUD);
        savedPolicy.setCoversClientOnly(false);

        Client client = new Client();
        client.setId(1L);
        savedPolicy.setClient(client);

        when(repository.save(any(HealthPolicy.class))).thenReturn(savedPolicy);

        HealthPolicy result = healthPolicyService.create(policyRequest);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getType()).isEqualTo(PolicyType.SALUD);
        assertThat(result.getCoversClientOnly()).isFalse();
        verify(repository).save(any(HealthPolicy.class));
    }

    @Test
    @DisplayName("Should create health policy with coversClientOnly true")
    void create_WithCoversClientOnlyTrue_ShouldReturnSavedPolicy() {
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

        when(repository.save(any(HealthPolicy.class))).thenReturn(savedPolicy);

        HealthPolicy result = healthPolicyService.create(request);

        assertThat(result.getCoversClientOnly()).isTrue();
        verify(repository).save(any(HealthPolicy.class));
    }
}
