package com.flowmanage.controller;

import com.flowmanage.dto.response.ApiResponse;
import com.flowmanage.dto.response.PagedResponse;
import com.flowmanage.dto.response.TaskResponse;
import com.flowmanage.security.AuthenticatedUser;
import com.flowmanage.common.PageableUtil;
import com.flowmanage.common.PaginationConstants;
import com.flowmanage.dto.request.CreateTaskRequest;
import com.flowmanage.dto.request.UpdateTaskRequest;
import com.flowmanage.util.TaskMapper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.flowmanage.service.TaskService;
import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Sort;

import java.util.UUID;

@Tag(name = "Tasks")
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
    @Operation(summary = "Create a new task in a project")
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
    @Operation(summary = "Get paginated list of tasks in a project")
    @GetMapping
    public ApiResponse<PagedResponse<TaskResponse>> getTasks(
            @PathVariable UUID projectId,
            Pageable pageable,
            @AuthenticationPrincipal AuthenticatedUser user) 
    {
        Pageable sanitized = PageableUtil.sanitize(
                pageable,
                PaginationConstants.DEFAULT_SORT_FIELD,
                Sort.Direction.DESC,
                PaginationConstants.PROJECT_SORT_FIELDS);
        
        Page<TaskResponse> page =
                taskService.getTasksByProject(projectId, user.getId(), sanitized)
                        .map(TaskMapper::toResponse);

        return new ApiResponse<>(
                PagedResponse.from(page)
        );
    }


    /*
     * =======================
     * UPDATE
     * =======================
     */
    @Operation(summary = "Update an existing task in a project")
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
    @Operation(summary = "Delete a task from a project")
    @DeleteMapping("/{taskId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTask(
            @PathVariable UUID projectId,
            @PathVariable UUID taskId,
            @AuthenticationPrincipal AuthenticatedUser user) {
        taskService.deleteTask(projectId, taskId, user.getId());
    }
}
