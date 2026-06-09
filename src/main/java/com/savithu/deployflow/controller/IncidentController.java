package com.savithu.deployflow.controller;

import com.savithu.deployflow.dto.*;
import com.savithu.deployflow.entity.IncidentStatus;
import com.savithu.deployflow.entity.IncidentType;
import com.savithu.deployflow.entity.Priority;
import com.savithu.deployflow.service.IncidentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/incidents")
public class IncidentController {

    private final IncidentService service;

    public IncidentController(IncidentService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<IncidentResponse> create(@Valid @RequestBody IncidentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @GetMapping
    public List<IncidentResponse> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public IncidentResponse findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PutMapping("/{id}")
    public IncidentResponse update(@PathVariable Long id, @Valid @RequestBody IncidentRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    public ApiSuccessResponse delete(@PathVariable Long id) {
        service.delete(id);
        return new ApiSuccessResponse(LocalDateTime.now(), HttpStatus.OK.value(), "Incident deleted successfully");
    }

    @GetMapping("/status/{status}")
    public List<IncidentResponse> findByStatus(@PathVariable IncidentStatus status) {
        return service.findByStatus(status);
    }

    @GetMapping("/priority/{priority}")
    public List<IncidentResponse> findByPriority(@PathVariable Priority priority) {
        return service.findByPriority(priority);
    }

    @GetMapping("/type/{incidentType}")
    public List<IncidentResponse> findByType(@PathVariable IncidentType incidentType) {
        return service.findByType(incidentType);
    }

    @PatchMapping("/{id}/resolve")
    public IncidentResponse resolve(@PathVariable Long id) {
        return service.resolve(id);
    }

    @GetMapping("/summary")
    public IncidentSummaryResponse summary() {
        return service.summary();
    }
}
