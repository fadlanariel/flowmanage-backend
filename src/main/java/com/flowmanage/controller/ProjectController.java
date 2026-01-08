package com.flowmanage.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Sort;

import com.flowmanage.common.PageableUtil;
import com.flowmanage.common.PaginationConstants;
import com.flowmanage.dto.request.CreateProjectRequest;
import com.flowmanage.dto.request.UpdateProjectRequest;
import com.flowmanage.dto.response.ApiResponse;
import com.flowmanage.dto.response.PagedResponse;
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

    @GetMapping
    public ApiResponse<PagedResponse<ProjectResponse>> getMyProjects(
        Pageable pageable,
        @AuthenticationPrincipal AuthenticatedUser user
    ) {
        Pageable sanitized = PageableUtil.sanitize(
                pageable,
                PaginationConstants.DEFAULT_SORT_FIELD,
                Sort.Direction.DESC,
                PaginationConstants.PROJECT_SORT_FIELDS);

        Page<ProjectResponse> page = projectService.getMyProjects(user.getId(), sanitized)
                .map(ProjectResponse::from);

        return new ApiResponse<>(
                PagedResponse.from(page));
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

    @PatchMapping("/{projectId}")
    public ProjectResponse updateProject(
            @PathVariable UUID projectId,
            @Valid @RequestBody UpdateProjectRequest request,
            @AuthenticationPrincipal AuthenticatedUser user
    ) {
        Project project = projectService.updateProject(
                projectId,
                user.getId(),
                request.getName(),
                request.getDescription()
        );

        return ProjectResponse.from(project);
    }

    @DeleteMapping("/{projectId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProject(
            @PathVariable UUID projectId,
            @AuthenticationPrincipal AuthenticatedUser user) {
        projectService.deleteProject(projectId, user.getId());
    }

}
