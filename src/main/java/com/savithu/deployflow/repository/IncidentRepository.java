package com.savithu.deployflow.repository;

import com.savithu.deployflow.entity.Incident;
import com.savithu.deployflow.entity.IncidentStatus;
import com.savithu.deployflow.entity.IncidentType;
import com.savithu.deployflow.entity.Priority;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface IncidentRepository extends JpaRepository<Incident, Long> {
    List<Incident> findByStatus(IncidentStatus status);
    List<Incident> findByPriority(Priority priority);
    List<Incident> findByIncidentType(IncidentType incidentType);
    long countByStatus(IncidentStatus status);
    long countByPriority(Priority priority);
}
