package com.flowmanage.service;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.flowmanage.entity.Project;
import com.flowmanage.exception.ProjectForbiddenException;
import com.flowmanage.exception.ProjectNotFoundException;
import com.flowmanage.repository.ProjectRepository;

@Service
@Transactional(readOnly = true)
public class ProjectService {
    private final ProjectRepository projectRepository;
    
    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public Project getProjectById(UUID projectId, UUID userId) {
        return projectRepository.findByIdAndOwnerId(projectId, userId)
                .orElseGet(() -> {
                    if (projectRepository.existsById(projectId)) {
                        throw new ProjectForbiddenException();
                    }

                    throw new ProjectNotFoundException();
                });
    }
}
