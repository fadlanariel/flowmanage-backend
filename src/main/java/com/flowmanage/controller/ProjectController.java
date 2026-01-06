package com.flowmanage.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.flowmanage.dto.request.CreateProjectRequest;
import com.flowmanage.dto.response.ProjectResponse;
import com.flowmanage.entity.Project;
import com.flowmanage.security.AuthenticatedUser;
import com.flowmanage.security.SecurityUtils;
import com.flowmanage.service.ProjectService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {
    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Project> getProjectById(@PathVariable UUID id) {
        AuthenticatedUser user = SecurityUtils.getCurrentUser();

        Project project = projectService.getProjectById(
            id, 
            user.getId()
        );

        return ResponseEntity.ok(project);
    }

    @PostMapping
    public ResponseEntity<ProjectResponse> createProject(
        @Valid @RequestBody CreateProjectRequest request
    ) {
        AuthenticatedUser user = SecurityUtils.getCurrentUser();

        Project project = projectService.createProject(
            user.getId(),
            request.name(),
            request.description()
        );

        return ResponseEntity.status(HttpStatus.CREATED)
            .body(new ProjectResponse(
                    project.getId(),
                    project.getName(),
                    project.getDescription()
            ));
    }        
}
