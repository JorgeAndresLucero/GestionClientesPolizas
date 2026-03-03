package com.insurance.api.controller;

import com.insurance.api.domain.Beneficiary;
import com.insurance.api.dto.BeneficiaryRequest;
import com.insurance.api.exception.BusinessException;
import com.insurance.api.service.BeneficiaryService;
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

@WebMvcTest(BeneficiaryController.class)
class BeneficiaryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BeneficiaryService beneficiaryService;

    @Autowired
    private ObjectMapper objectMapper;

    private Beneficiary testBeneficiary;
    private BeneficiaryRequest request;

    @BeforeEach
    void setUp() {
        testBeneficiary = new Beneficiary();
        testBeneficiary.setId(1L);
        testBeneficiary.setName("John Doe");
        testBeneficiary.setRelationship("Spouse");

        request = new BeneficiaryRequest();
        request.setName("John Doe");
        request.setRelationship("Spouse");
    }

    @Test
    @DisplayName("Should return beneficiaries by policy ID")
    void getBeneficiaries_WhenPolicyHasBeneficiaries_ShouldReturnList() throws Exception {
        List<Beneficiary> beneficiaries = Arrays.asList(
                createBeneficiary(1L, "John Doe", "Spouse"),
                createBeneficiary(2L, "Jane Doe", "Child")
        );

        when(beneficiaryService.getByPolicy(1L)).thenReturn(beneficiaries);

        mockMvc.perform(get("/policies/life/1/beneficiaries"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("John Doe"))
                .andExpect(jsonPath("$[1].name").value("Jane Doe"));
    }

    @Test
    @DisplayName("Should return 400 when policy has no beneficiaries")
    void getBeneficiaries_WhenPolicyHasNoBeneficiaries_ShouldReturnBadRequest() throws Exception {
        when(beneficiaryService.getByPolicy(1L))
                .thenThrow(new BusinessException("No beneficiaries found for this policy"));

        mockMvc.perform(get("/policies/life/1/beneficiaries"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return all beneficiaries")
    void findAll_ShouldReturnListOfAllBeneficiaries() throws Exception {
        List<Beneficiary> beneficiaries = Arrays.asList(
                createBeneficiary(1L, "John Doe", "Spouse"),
                createBeneficiary(2L, "Jane Doe", "Child"),
                createBeneficiary(3L, "Bob Smith", "Parent")
        );

        when(beneficiaryService.findAll()).thenReturn(beneficiaries);

        mockMvc.perform(get("/policies/beneficiaries"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].name").value("John Doe"))
                .andExpect(jsonPath("$[2].name").value("Bob Smith"));
    }

    @Test
    @DisplayName("Should return empty list when no beneficiaries exist")
    void findAll_WhenNoBeneficiariesExist_ShouldReturnEmptyList() throws Exception {
        when(beneficiaryService.findAll()).thenReturn(Arrays.asList());

        mockMvc.perform(get("/policies/beneficiaries"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    @DisplayName("Should add beneficiary to policy successfully")
    void addBeneficiary_WhenPolicyExists_ShouldReturnCreatedBeneficiary() throws Exception {
        when(beneficiaryService.addBeneficiary(anyLong(), any(BeneficiaryRequest.class)))
                .thenReturn(testBeneficiary);

        mockMvc.perform(post("/policies/life/1/beneficiaries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.relationship").value("Spouse"));
    }

    @Test
    @DisplayName("Should return 400 when policy not found for adding beneficiary")
    void addBeneficiary_WhenPolicyNotFound_ShouldReturnBadRequest() throws Exception {
        when(beneficiaryService.addBeneficiary(anyLong(), any(BeneficiaryRequest.class)))
                .thenThrow(new BusinessException("Life policy not found"));

        mockMvc.perform(post("/policies/life/1/beneficiaries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 400 when policy already has 2 beneficiaries")
    void addBeneficiary_WhenPolicyHas2Beneficiaries_ShouldReturnBadRequest() throws Exception {
        when(beneficiaryService.addBeneficiary(anyLong(), any(BeneficiaryRequest.class)))
                .thenThrow(new BusinessException("Life policy can only have 2 beneficiaries"));

        mockMvc.perform(post("/policies/life/1/beneficiaries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    private Beneficiary createBeneficiary(Long id, String name, String relationship) {
        Beneficiary beneficiary = new Beneficiary();
        beneficiary.setId(id);
        beneficiary.setName(name);
        beneficiary.setRelationship(relationship);
        return beneficiary;
    }
}
