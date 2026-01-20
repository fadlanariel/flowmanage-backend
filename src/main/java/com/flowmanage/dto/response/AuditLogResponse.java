package com.flowmanage.dto.response;

import java.time.Instant;
import java.util.UUID;

import com.flowmanage.entity.AuditAction;
import com.flowmanage.entity.AuditLog;
import com.flowmanage.entity.EntityType;

public record AuditLogResponse(
        UUID id,
        UUID userId,
        UUID projectId,
        UUID taskId,
        AuditAction action,
        EntityType entityType,
        Instant createdAt) {
    public static AuditLogResponse from(AuditLog log) {
        return new AuditLogResponse(
                log.getId(),
                log.getUserId(),
                log.getProjectId(),
                log.getTaskId(),
                log.getAction(),
                log.getEntityType(),
                log.getCreatedAt());
    }
}
