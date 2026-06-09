package com.savithu.deployflow.dto;

public record IncidentSummaryResponse(
        long totalIncidents,
        long openIncidents,
        long inProgressIncidents,
        long criticalIncidents,
        long resolvedIncidents,
        long cancelledIncidents
) {
}
