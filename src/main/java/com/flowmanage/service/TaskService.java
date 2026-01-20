package com.flowmanage.service;

import com.flowmanage.entity.Task;
import com.flowmanage.entity.TaskStatus;
import com.flowmanage.exception.TaskNotFoundException;
import com.flowmanage.repository.TaskRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class TaskService {
    
    private final TaskRepository taskRepository;
    private final ProjectService projectService;
    private final AuditLogService auditLogService;

    public TaskService(
            TaskRepository taskRepository,
            ProjectService projectService,
            AuditLogService auditLogService) {
        this.taskRepository = taskRepository;
        this.projectService = projectService;
        this.auditLogService = auditLogService;
    }

    /* ============================
       CREATE
       ============================ */

    @Transactional
    public Task createTask(
        UUID projectId,
        UUID userId,
        String title,
        String description
    ) {
        projectService.validateOwnership(projectId, userId);

        Task task = new Task();
        task.setProjectId(projectId);
        task.setTitle(title);
        task.setDescription(description);

        Task saved = taskRepository.save(task);

        auditLogService.logTaskCreated(userId, projectId, saved.getId());

        return saved;
    }
    
    /*
     * ============================
     * READ
     * ============================
     */

    @Transactional(readOnly = true)
    public Page<Task> getTasksByProject(UUID projectId, UUID userId, Pageable pageable) {
        projectService.validateOwnership(projectId, userId);

        return taskRepository.findAllByProjectId(projectId, pageable);
    }

    @Transactional(readOnly = true)
    public Task getTaskById(
            UUID projectId,
            UUID taskId,
            UUID userId) {
        projectService.validateOwnership(projectId, userId);

        return taskRepository.findByIdAndProjectId(taskId, projectId)
                .orElseThrow(TaskNotFoundException::new);
    }

    /*
     * ============================
     * UPDATE
     * ============================
     */

    @Transactional
    public Task updateTask(
            UUID projectId,
            UUID taskId,
            UUID userId,
            String title,
            String description,
            TaskStatus status) {
        projectService.validateOwnership(projectId, userId);

        Task task = taskRepository.findByIdAndProjectId(taskId, projectId)
                .orElseThrow(TaskNotFoundException::new);

        if (title != null) {
            task.setTitle(title);
        }
        if (description != null) {
            task.setDescription(description);
        }
        if (status != null) {
            task.setStatus(status);
        }

        auditLogService.logTaskUpdated(userId, projectId, taskId);

        return task; // Hibernate dirty checking
    }

    /*
     * ============================
     * DELETE
     * ============================
     */

    @Transactional
    public void deleteTask(
            UUID projectId,
            UUID taskId,
            UUID userId) {
        projectService.validateOwnership(projectId, userId);

        Task task = taskRepository.findByIdAndProjectId(taskId, projectId)
                .orElseThrow(TaskNotFoundException::new);

        taskRepository.delete(task);

        auditLogService.logTaskDeleted(userId, projectId, taskId);
    }
}
