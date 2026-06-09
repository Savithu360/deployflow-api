package com.savithu.deployflow.service;

import com.savithu.deployflow.dto.IncidentRequest;
import com.savithu.deployflow.dto.IncidentResponse;
import com.savithu.deployflow.dto.IncidentSummaryResponse;
import com.savithu.deployflow.entity.*;
import com.savithu.deployflow.exception.IncidentNotFoundException;
import com.savithu.deployflow.repository.IncidentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class IncidentService {

    private static final Logger log = LoggerFactory.getLogger(IncidentService.class);
    private final IncidentRepository repository;

    public IncidentService(IncidentRepository repository) {
        this.repository = repository;
    }

    public IncidentResponse create(IncidentRequest request) {
        Incident incident = new Incident();
        applyRequest(incident, request);
        Incident saved = repository.save(incident);
        log.info("Created incident id={} priority={}", saved.getId(), saved.getPriority());
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<IncidentResponse> findAll() {
        return repository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public IncidentResponse findById(Long id) {
        return toResponse(getIncident(id));
    }

    public IncidentResponse update(Long id, IncidentRequest request) {
        Incident incident = getIncident(id);
        applyRequest(incident, request);
        if (request.status() == IncidentStatus.RESOLVED && incident.getResolvedAt() == null) {
            incident.setResolvedAt(LocalDateTime.now());
        } else if (request.status() != IncidentStatus.RESOLVED) {
            incident.setResolvedAt(null);
        }
        Incident saved = repository.save(incident);
        log.info("Updated incident id={}", id);
        return toResponse(saved);
    }

    public void delete(Long id) {
        Incident incident = getIncident(id);
        repository.delete(incident);
        log.info("Deleted incident id={}", id);
    }

    @Transactional(readOnly = true)
    public List<IncidentResponse> findByStatus(IncidentStatus status) {
        return map(repository.findByStatus(status));
    }

    @Transactional(readOnly = true)
    public List<IncidentResponse> findByPriority(Priority priority) {
        return map(repository.findByPriority(priority));
    }

    @Transactional(readOnly = true)
    public List<IncidentResponse> findByType(IncidentType type) {
        return map(repository.findByIncidentType(type));
    }

    public IncidentResponse resolve(Long id) {
        Incident incident = getIncident(id);
        incident.setStatus(IncidentStatus.RESOLVED);
        if (incident.getResolvedAt() == null) {
            incident.setResolvedAt(LocalDateTime.now());
        }
        Incident saved = repository.save(incident);
        log.info("Resolved incident id={}", id);
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public IncidentSummaryResponse summary() {
        return new IncidentSummaryResponse(
                repository.count(),
                repository.countByStatus(IncidentStatus.OPEN),
                repository.countByStatus(IncidentStatus.IN_PROGRESS),
                repository.countByPriority(Priority.CRITICAL),
                repository.countByStatus(IncidentStatus.RESOLVED),
                repository.countByStatus(IncidentStatus.CANCELLED)
        );
    }

    private Incident getIncident(Long id) {
        return repository.findById(id).orElseThrow(() -> new IncidentNotFoundException(id));
    }

    private void applyRequest(Incident incident, IncidentRequest request) {
        incident.setTitle(request.title());
        incident.setDescription(request.description());
        incident.setIncidentType(request.incidentType());
        incident.setPriority(request.priority());
        incident.setStatus(request.status());
        incident.setLocation(request.location());
        incident.setVehicleId(request.vehicleId());
        incident.setReportedBy(request.reportedBy());
    }

    private List<IncidentResponse> map(List<Incident> incidents) {
        return incidents.stream().map(this::toResponse).toList();
    }

    private IncidentResponse toResponse(Incident incident) {
        return new IncidentResponse(
                incident.getId(), incident.getTitle(), incident.getDescription(),
                incident.getIncidentType(), incident.getPriority(), incident.getStatus(),
                incident.getLocation(), incident.getVehicleId(), incident.getReportedBy(),
                incident.getCreatedAt(), incident.getUpdatedAt(), incident.getResolvedAt()
        );
    }
}
