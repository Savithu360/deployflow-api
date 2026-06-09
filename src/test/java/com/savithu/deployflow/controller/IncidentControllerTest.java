package com.savithu.deployflow.controller;

import com.savithu.deployflow.repository.IncidentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.ActiveProfiles;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class IncidentControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired IncidentRepository repository;

    @BeforeEach
    void clearDatabase() {
        repository.deleteAll();
    }

    @Test
    void createsIncidentAndReturnsSummary() throws Exception {
        mockMvc.perform(post("/api/incidents")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validRequest()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.priority").value("HIGH"));

        mockMvc.perform(get("/api/incidents/summary"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalIncidents").value(1));
    }

    @Test
    void rejectsInvalidRequestAndEnum() throws Exception {
        mockMvc.perform(post("/api/incidents")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors.title").exists());

        mockMvc.perform(get("/api/incidents/status/UNKNOWN"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void returnsNotFoundForMissingIncident() throws Exception {
        mockMvc.perform(get("/api/incidents/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void exposesHealthAndInfoEndpoints() throws Exception {
        mockMvc.perform(get("/api/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"));

        mockMvc.perform(get("/actuator/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"));

        mockMvc.perform(get("/actuator/info"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.app.name").value("DeployFlow API"));
    }

    private String validRequest() {
        return """
                {
                  "title": "Truck delayed due to road closure",
                  "description": "Unexpected road closure.",
                  "incidentType": "DELIVERY_DELAY",
                  "priority": "HIGH",
                  "status": "OPEN",
                  "location": "Colombo Route 04",
                  "vehicleId": "TRUCK-102",
                  "reportedBy": "Operations Team"
                }
                """;
    }
}
