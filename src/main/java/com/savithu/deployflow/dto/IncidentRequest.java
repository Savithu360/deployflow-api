package com.savithu.deployflow.dto;

import com.savithu.deployflow.entity.IncidentStatus;
import com.savithu.deployflow.entity.IncidentType;
import com.savithu.deployflow.entity.Priority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record IncidentRequest(
        @NotBlank(message = "Title must not be blank") String title,
        String description,
        @NotNull(message = "Incident type is required") IncidentType incidentType,
        @NotNull(message = "Priority is required") Priority priority,
        @NotNull(message = "Status is required") IncidentStatus status,
        @NotBlank(message = "Location must not be blank") String location,
        String vehicleId,
        @NotBlank(message = "Reported by must not be blank") String reportedBy
) {
}
