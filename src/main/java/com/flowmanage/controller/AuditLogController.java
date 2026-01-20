package com.flowmanage.controller;

import java.util.Set;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.data.domain.Sort;

import com.flowmanage.common.PageableUtil;
import com.flowmanage.dto.response.ApiResponse;
import com.flowmanage.dto.response.AuditLogResponse;
import com.flowmanage.dto.response.PagedResponse;
import com.flowmanage.entity.AuditAction;
import com.flowmanage.service.AuditLogService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/audit-logs")
@RequiredArgsConstructor
public class AuditLogController {

    private final AuditLogService auditLogService;

    @GetMapping
    // @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<PagedResponse<AuditLogResponse>> getAuditLogs(
            @RequestParam(required = false) UUID projectId,
            @RequestParam(required = false) UUID taskId,
            @RequestParam(required = false) UUID userId,
            @RequestParam(required = false) AuditAction action,
            Pageable pageable
    ) {
        Pageable sanitizedPageable = PageableUtil.sanitize(
            pageable,
            "createdAt",
            Sort.Direction.DESC,
            Set.of(
                "createdAt",
                "action",
                "entityType",
                "userId",
                "projectId"
            )
        );

        Page<AuditLogResponse> page =
            auditLogService.getAuditLogs(
                projectId, taskId, userId, action, sanitizedPageable
            ).map(AuditLogResponse::from);

        return new ApiResponse<>(PagedResponse.from(page));
    }

}
