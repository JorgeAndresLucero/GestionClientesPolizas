package com.insurance.api.controller;

import com.insurance.api.domain.HealthMember;
import com.insurance.api.domain.HealthPolicy;
import com.insurance.api.domain.PolicyType;
import com.insurance.api.dto.HealthMemberRequest;
import com.insurance.api.dto.HealthPolicyRequest;
import com.insurance.api.exception.BusinessException;
import com.insurance.api.service.HealthPolicyService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HealthPolicyController.class)
class HealthPolicyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HealthPolicyService healthPolicyService;

    @Autowired
    private ObjectMapper objectMapper;

    private HealthPolicy testPolicy;
    private HealthMember testMember;
    private HealthPolicyRequest policyRequest;
    private HealthMemberRequest memberRequest;

    @BeforeEach
    void setUp() {
        testPolicy = new HealthPolicy();
        testPolicy.setId(1L);
        testPolicy.setType(PolicyType.SALUD);
        testPolicy.setCoversClientOnly(false);

        testMember = new HealthMember();
        testMember.setId(1L);
        testMember.setName("John Doe");
        testMember.setRelationship("SPOUSE");

        policyRequest = new HealthPolicyRequest();
        policyRequest.setClientId(1L);
        policyRequest.setCoversClientOnly(false);

        memberRequest = new HealthMemberRequest();
        memberRequest.setName("John Doe");
        memberRequest.setRelationship("SPOUSE");
    }

    @Test
    @DisplayName("Should create health policy successfully")
    void create_ShouldReturnCreatedPolicy() throws Exception {
        when(healthPolicyService.create(any(HealthPolicyRequest.class))).thenReturn(testPolicy);

        mockMvc.perform(post("/policies/health")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(policyRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.type").value("SALUD"));
    }

    @Test
    @DisplayName("Should return members by policy ID")
    void getMembers_WhenPolicyExists_ShouldReturnMembersList() throws Exception {
        List<HealthMember> members = Arrays.asList(
                createMember(1L, "John Doe", "SPOUSE"),
                createMember(2L, "Jane Doe", "CHILD")
        );

        when(healthPolicyService.getMembers(1L)).thenReturn(members);

        mockMvc.perform(get("/policies/health/1/members"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("John Doe"))
                .andExpect(jsonPath("$[1].name").value("Jane Doe"));
    }

    @Test
    @DisplayName("Should return 400 when policy not found for members")
    void getMembers_WhenPolicyNotFound_ShouldReturnBadRequest() throws Exception {
        when(healthPolicyService.getMembers(1L))
                .thenThrow(new BusinessException("Health policy not found"));

        mockMvc.perform(get("/policies/health/1/members"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should add member to policy successfully")
    void addMember_WhenPolicyExists_ShouldReturnCreatedMember() throws Exception {
        when(healthPolicyService.addMember(anyLong(), any(HealthMemberRequest.class)))
                .thenReturn(testMember);

        mockMvc.perform(post("/policies/health/1/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.relationship").value("SPOUSE"));
    }

    @Test
    @DisplayName("Should return 400 when policy not found for adding member")
    void addMember_WhenPolicyNotFound_ShouldReturnBadRequest() throws Exception {
        when(healthPolicyService.addMember(anyLong(), any(HealthMemberRequest.class)))
                .thenThrow(new BusinessException("Health policy not found"));

        mockMvc.perform(post("/policies/health/1/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 400 when policy covers client only")
    void addMember_WhenPolicyCoversClientOnly_ShouldReturnBadRequest() throws Exception {
        when(healthPolicyService.addMember(anyLong(), any(HealthMemberRequest.class)))
                .thenThrow(new BusinessException("This policy only covers the client"));

        mockMvc.perform(post("/policies/health/1/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should create health policy with coversClientOnly true")
    void create_WithCoversClientOnlyTrue_ShouldReturnCreatedPolicy() throws Exception {
        HealthPolicyRequest request = new HealthPolicyRequest();
        request.setClientId(1L);
        request.setCoversClientOnly(true);

        HealthPolicy policy = new HealthPolicy();
        policy.setId(1L);
        policy.setType(PolicyType.SALUD);
        policy.setCoversClientOnly(true);

        when(healthPolicyService.create(any(HealthPolicyRequest.class))).thenReturn(policy);

        mockMvc.perform(post("/policies/health")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.coversClientOnly").value(true));
    }

    private HealthMember createMember(Long id, String name, String relationship) {
        HealthMember member = new HealthMember();
        member.setId(id);
        member.setName(name);
        member.setRelationship(relationship);
        return member;
    }
}
