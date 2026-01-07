package com.flowmanage.service;

import com.flowmanage.entity.Task;
import com.flowmanage.entity.TaskStatus;
import com.flowmanage.exception.TaskNotFoundException;
import com.flowmanage.repository.TaskRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class TaskService {
    
    private final TaskRepository taskRepository;
    private final ProjectService projectService;

    public TaskService(
            TaskRepository taskRepository,
            ProjectService projectService) {
        this.taskRepository = taskRepository;
        this.projectService = projectService;
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

        return taskRepository.save(task);
    }
    
    /*
     * ============================
     * READ
     * ============================
     */

    @Transactional(readOnly = true)
    public List<Task> getTasksByProject(UUID projectId, UUID userId) {
        projectService.validateOwnership(projectId, userId);

        return taskRepository.findAllByProjectId(projectId);
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

        task.setTitle(title);
        task.setDescription(description);
        task.setStatus(status);

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
    }
}
