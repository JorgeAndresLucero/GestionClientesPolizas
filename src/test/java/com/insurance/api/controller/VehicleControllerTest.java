package com.insurance.api.controller;

import com.insurance.api.domain.Vehicle;
import com.insurance.api.domain.VehiclePolicy;
import com.insurance.api.domain.PolicyType;
import com.insurance.api.dto.VehiclePolicyRequest;
import com.insurance.api.dto.VehicleRequest;
import com.insurance.api.exception.BusinessException;
import com.insurance.api.service.VehicleService;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VehicleController.class)
class VehicleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VehicleService vehicleService;

    @Autowired
    private ObjectMapper objectMapper;

    private VehiclePolicy testPolicy;
    private Vehicle testVehicle;
    private VehiclePolicyRequest policyRequest;
    private VehicleRequest vehicleRequest;

    @BeforeEach
    void setUp() {
        testPolicy = new VehiclePolicy();
        testPolicy.setId(1L);
        testPolicy.setType(PolicyType.VEHICULO);

        testVehicle = new Vehicle();
        testVehicle.setId(1L);
        testVehicle.setPlate("ABC123");
        testVehicle.setBrand("Toyota");
        testVehicle.setModel("Corolla");
        testVehicle.setVehicleYear(2023);

        policyRequest = new VehiclePolicyRequest();
        policyRequest.setClientId(1L);

        vehicleRequest = new VehicleRequest();
        vehicleRequest.setPlate("ABC123");
        vehicleRequest.setBrand("Toyota");
        vehicleRequest.setModel("Corolla");
        vehicleRequest.setVehicleYear(2023);
    }

    @Test
    @DisplayName("Should create vehicle policy successfully")
    void createVehiclePolicy_ShouldReturnCreatedPolicy() throws Exception {
        when(vehicleService.createVehiclePolicy(any(VehiclePolicyRequest.class))).thenReturn(testPolicy);

        mockMvc.perform(post("/policies/vehicle")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(policyRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.type").value("VEHICULO"));
    }

    @Test
    @DisplayName("Should add vehicle to policy successfully")
    void addVehicle_WhenPolicyExists_ShouldReturnCreatedVehicle() throws Exception {
        when(vehicleService.addVehicle(anyLong(), any(VehicleRequest.class))).thenReturn(testVehicle);

        mockMvc.perform(post("/policies/vehicle/1/vehicles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(vehicleRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.plate").value("ABC123"))
                .andExpect(jsonPath("$.brand").value("Toyota"))
                .andExpect(jsonPath("$.model").value("Corolla"))
                .andExpect(jsonPath("$.vehicleYear").value(2023));
    }

    @Test
    @DisplayName("Should return 400 when policy not found for adding vehicle")
    void addVehicle_WhenPolicyNotFound_ShouldReturnBadRequest() throws Exception {
        when(vehicleService.addVehicle(anyLong(), any(VehicleRequest.class)))
                .thenThrow(new BusinessException("Vehicle policy not found"));

        mockMvc.perform(post("/policies/vehicle/1/vehicles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(vehicleRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return 400 when invalid policy type")
    void addVehicle_WhenInvalidPolicyType_ShouldReturnBadRequest() throws Exception {
        when(vehicleService.addVehicle(anyLong(), any(VehicleRequest.class)))
                .thenThrow(new BusinessException("Invalid policy type"));

        mockMvc.perform(post("/policies/vehicle/1/vehicles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(vehicleRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should create vehicle policy with different client")
    void createVehiclePolicy_WithDifferentClient_ShouldReturnCreatedPolicy() throws Exception {
        VehiclePolicyRequest request = new VehiclePolicyRequest();
        request.setClientId(2L);

        VehiclePolicy policy = new VehiclePolicy();
        policy.setId(2L);
        policy.setType(PolicyType.VEHICULO);

        when(vehicleService.createVehiclePolicy(any(VehiclePolicyRequest.class))).thenReturn(policy);

        mockMvc.perform(post("/policies/vehicle")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2L));
    }

    @Test
    @DisplayName("Should add vehicle with minimal required fields")
    void addVehicle_WithMinimalFields_ShouldReturnCreatedVehicle() throws Exception {
        VehicleRequest request = new VehicleRequest();
        request.setPlate("XYZ789");
        request.setVehicleYear(2024);

        Vehicle vehicle = new Vehicle();
        vehicle.setId(2L);
        vehicle.setPlate("XYZ789");
        vehicle.setVehicleYear(2024);

        when(vehicleService.addVehicle(anyLong(), any(VehicleRequest.class))).thenReturn(vehicle);

        mockMvc.perform(post("/policies/vehicle/1/vehicles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.plate").value("XYZ789"))
                .andExpect(jsonPath("$.vehicleYear").value(2024));
    }
}
