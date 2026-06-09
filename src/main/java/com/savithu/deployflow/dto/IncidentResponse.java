package com.savithu.deployflow.dto;

import com.savithu.deployflow.entity.IncidentStatus;
import com.savithu.deployflow.entity.IncidentType;
import com.savithu.deployflow.entity.Priority;
import java.time.LocalDateTime;

public record IncidentResponse(
        Long id,
        String title,
        String description,
        IncidentType incidentType,
        Priority priority,
        IncidentStatus status,
        String location,
        String vehicleId,
        String reportedBy,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime resolvedAt
) {
}
