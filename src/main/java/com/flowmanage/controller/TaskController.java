package com.flowmanage.controller;

import com.flowmanage.dto.response.TaskResponse;
import com.flowmanage.security.AuthenticatedUser;
import com.flowmanage.dto.request.CreateTaskRequest;
import com.flowmanage.dto.request.UpdateTaskRequest;
import com.flowmanage.util.TaskMapper;
import com.flowmanage.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/projects/{projectId}/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    /*
     * =======================
     * CREATE
     * =======================
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskResponse createTask(
            @PathVariable UUID projectId,
            @RequestBody @Valid CreateTaskRequest request,
            @AuthenticationPrincipal AuthenticatedUser user) {
        return TaskMapper.toResponse(
                taskService.createTask(
                        projectId,
                        user.getId(),
                        request.title(),
                        request.description()));
    }

    /*
     * =======================
     * LIST
     * =======================
     */
    @GetMapping
    public List<TaskResponse> getTasks(
            @PathVariable UUID projectId, 
            @AuthenticationPrincipal AuthenticatedUser user) {
        return taskService.getTasksByProject(projectId, user.getId())
                .stream()
                .map(TaskMapper::toResponse)
                .toList();
    }

    /*
     * =======================
     * UPDATE
     * =======================
     */
    @PatchMapping("/{taskId}")
    public TaskResponse updateTask(
            @PathVariable UUID projectId,
            @PathVariable UUID taskId,
            @RequestBody @Valid UpdateTaskRequest request,
            @AuthenticationPrincipal AuthenticatedUser user) 
    {
        return TaskMapper.toResponse(
            taskService.updateTask(
                projectId,
                taskId,
                user.getId(),
                request.title(),
                request.description(),
                request.status()
            )
        );
    }

    /*
     * =======================
     * DELETE
     * =======================
     */
    @DeleteMapping("/{taskId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTask(
            @PathVariable UUID projectId,
            @PathVariable UUID taskId,
            @AuthenticationPrincipal AuthenticatedUser user) {
        taskService.deleteTask(projectId, taskId, user.getId());
    }
}
