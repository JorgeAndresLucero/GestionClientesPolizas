package com.insurance.api.controller;

import com.insurance.api.domain.LifePolicy;
import com.insurance.api.domain.PolicyType;
import com.insurance.api.dto.LifePolicyRequest;
import com.insurance.api.exception.BusinessException;
import com.insurance.api.service.LifePolicyService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LifePolicyController.class)
class LifePolicyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LifePolicyService lifePolicyService;

    @Autowired
    private ObjectMapper objectMapper;

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
    }

    @Test
    @DisplayName("Should create life policy successfully")
    void create_ShouldReturnCreatedPolicy() throws Exception {
        when(lifePolicyService.createLifePolicy(any(LifePolicyRequest.class))).thenReturn(testPolicy);

        mockMvc.perform(post("/policies/life")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.type").value("VIDA"))
                .andExpect(jsonPath("$.insuredAmount").value(100000.0));
    }

    @Test
    @DisplayName("Should return 400 when client already has life policy")
    void create_WhenClientAlreadyHasPolicy_ShouldReturnBadRequest() throws Exception {
        when(lifePolicyService.createLifePolicy(any(LifePolicyRequest.class)))
                .thenThrow(new BusinessException("Client already has a life policy"));

        mockMvc.perform(post("/policies/life")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should create life policy with different insured amount")
    void create_WithDifferentInsuredAmount_ShouldReturnCreatedPolicy() throws Exception {
        LifePolicyRequest customRequest = new LifePolicyRequest();
        customRequest.setClientId(2L);
        customRequest.setInsuredAmount(250000.0);

        LifePolicy savedPolicy = new LifePolicy();
        savedPolicy.setId(2L);
        savedPolicy.setType(PolicyType.VIDA);
        savedPolicy.setInsuredAmount(250000.0);

        when(lifePolicyService.createLifePolicy(any(LifePolicyRequest.class))).thenReturn(savedPolicy);

        mockMvc.perform(post("/policies/life")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.insuredAmount").value(250000.0));
    }
}
