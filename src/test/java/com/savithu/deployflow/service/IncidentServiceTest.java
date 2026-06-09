package com.savithu.deployflow.service;

import com.savithu.deployflow.dto.IncidentRequest;
import com.savithu.deployflow.dto.IncidentResponse;
import com.savithu.deployflow.entity.*;
import com.savithu.deployflow.repository.IncidentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class IncidentServiceTest {

    @Autowired IncidentService service;
    @Autowired IncidentRepository repository;

    @BeforeEach
    void clearDatabase() {
        repository.deleteAll();
    }

    @Test
    void createsAndResolvesIncident() {
        IncidentResponse created = service.create(request(Priority.CRITICAL));

        IncidentResponse resolved = service.resolve(created.id());

        assertThat(created.id()).isNotNull();
        assertThat(resolved.status()).isEqualTo(IncidentStatus.RESOLVED);
        assertThat(resolved.resolvedAt()).isNotNull();
        assertThat(service.summary().criticalIncidents()).isEqualTo(1);
        assertThat(service.summary().resolvedIncidents()).isEqualTo(1);
    }

    private IncidentRequest request(Priority priority) {
        return new IncidentRequest(
                "Cold storage temperature alert", "Temperature exceeded safe range.",
                IncidentType.TEMPERATURE_ALERT, priority, IncidentStatus.OPEN,
                "Warehouse Zone A", "TRUCK-218", "Warehouse Supervisor"
        );
    }
}
