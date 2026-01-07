package com.flowmanage.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.flowmanage.entity.Project;
import com.flowmanage.exception.ProjectForbiddenException;
import com.flowmanage.exception.ProjectNotFoundException;
import com.flowmanage.repository.ProjectRepository;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;
    
    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Transactional(readOnly = true)
    public Project getProjectById(UUID projectId, UUID userId) {
        return projectRepository.findByIdAndOwnerId(projectId, userId)
                .orElseGet(() -> {
                    if (projectRepository.existsById(projectId)) {
                        throw new ProjectForbiddenException();
                    }

                    throw new ProjectNotFoundException();
                });
    }

    @Transactional(readOnly = true)
    public List<Project> getMyProjects(UUID userId) {
        return projectRepository.findAllByOwnerId(userId);
    }

    @Transactional
    public Project createProject(
        UUID ownerId,
        String name,
        String description
    ) {
        Project project = new Project();
        project.setOwnerId(ownerId);
        project.setName(name);
        project.setDescription(description);
        
        return projectRepository.save(project);
    }

    @Transactional
    public Project updateProject(
            UUID projectId,
            UUID userId,
            String name,
            String description) {
        Project project = getProjectById(projectId, userId);

        if (name != null) {
            project.setName(name);
        }

        if (description != null) {
            project.setDescription(description);
        }

        return projectRepository.save(project);
    }

    @Transactional
    public void deleteProject(UUID projectId, UUID userId) {
        Project project = getProjectById(projectId, userId);
        projectRepository.delete(project);
    }

    @Transactional(readOnly = true)
    public void validateOwnership(UUID projectId, UUID userId) {
        if (projectRepository.existsByIdAndOwnerId(projectId, userId)) {
            return;
        }

        if (projectRepository.existsById(projectId)) {
            throw new ProjectForbiddenException();
        }

        throw new ProjectNotFoundException();
    }
}
