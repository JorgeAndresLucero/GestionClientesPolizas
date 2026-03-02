package com.insurance.api.controller;

import com.insurance.api.domain.Client;
import com.insurance.api.domain.LifePolicy;
import com.insurance.api.domain.Policy;
import com.insurance.api.domain.PolicyType;
import com.insurance.api.exception.BusinessException;
import com.insurance.api.service.PolicyService;
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

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PolicyController.class)
class PolicyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PolicyService policyService;

    @Autowired
    private ObjectMapper objectMapper;

    private Policy testPolicy;
    private Client testClient;

    @BeforeEach
    void setUp() {
        testClient = new Client();
        testClient.setId(1L);
        testClient.setFirstName("John");
        testClient.setLastName("Doe");

        testPolicy = createLifePolicy(1L, testClient, 100000.0);
    }

    @Test
    @DisplayName("Should return policies by client ID")
    void getPolicies_WhenClientHasPolicies_ShouldReturnList() throws Exception {
        List<Policy> policies = Arrays.asList(
                createLifePolicy(1L, testClient, 100000.0),
                createLifePolicy(2L, testClient, 200000.0)
        );

        when(policyService.getPoliciesByClient(1L)).thenReturn(policies);

        mockMvc.perform(get("/policies/client/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].type").value("VIDA"))
                .andExpect(jsonPath("$[1].id").value(2L));
    }

    @Test
    @DisplayName("Should return empty list when client has no policies")
    void getPolicies_WhenClientHasNoPolicies_ShouldReturnEmptyList() throws Exception {
        when(policyService.getPoliciesByClient(1L)).thenReturn(Arrays.asList());

        mockMvc.perform(get("/policies/client/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    @DisplayName("Should return policy when found by ID")
    void getPolicy_WhenPolicyExists_ShouldReturnPolicy() throws Exception {
        when(policyService.getPolicy(1L)).thenReturn(testPolicy);

        mockMvc.perform(get("/policies/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.type").value("VIDA"))
                .andExpect(jsonPath("$.insuredAmount").value(100000.0));
    }

    @Test
    @DisplayName("Should return 400 when policy not found")
    void getPolicy_WhenPolicyNotFound_ShouldReturnBadRequest() throws Exception {
        when(policyService.getPolicy(1L))
                .thenThrow(new BusinessException("Policy not found"));

        mockMvc.perform(get("/policies/1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should delete policy successfully")
    void deletePolicy_WhenPolicyExists_ShouldReturnNoContent() throws Exception {
        doNothing().when(policyService).deletePolicy(1L);

        mockMvc.perform(delete("/policies/1"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should return 400 when deleting non-existent policy")
    void deletePolicy_WhenPolicyNotFound_ShouldReturnBadRequest() throws Exception {
        doThrow(new BusinessException("Policy not found"))
                .when(policyService).deletePolicy(1L);

        mockMvc.perform(delete("/policies/1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return multiple policy types for client")
    void getPolicies_WithMultiplePolicyTypes_ShouldReturnAllPolicies() throws Exception {
        LifePolicy lifePolicy = createLifePolicy(1L, testClient, 100000.0);

        when(policyService.getPoliciesByClient(1L)).thenReturn(Arrays.asList(lifePolicy));

        mockMvc.perform(get("/policies/client/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].type").value("VIDA"));
    }

    @Test
    @DisplayName("Should return policy with client information")
    void getPolicy_ShouldReturnPolicyWithClient() throws Exception {
        when(policyService.getPolicy(1L)).thenReturn(testPolicy);

        mockMvc.perform(get("/policies/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.client.id").value(1L))
                .andExpect(jsonPath("$.client.firstName").value("John"));
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
