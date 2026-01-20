package com.flowmanage.service;

import java.time.Instant;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.flowmanage.entity.AuditAction;
import com.flowmanage.entity.AuditLog;
import com.flowmanage.entity.EntityType;
import com.flowmanage.repository.AuditLogRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    /*
     * =========================
     * PROJECT LEVEL
     * =========================
     */

    @Transactional
    public void logProjectCreated(UUID userId, UUID projectId) {
        save(userId, projectId, null, AuditAction.CREATE, EntityType.PROJECT);
    }

    @Transactional
    public void logProjectUpdated(UUID userId, UUID projectId) {
        save(userId, projectId, null, AuditAction.UPDATE, EntityType.PROJECT);
    }

    @Transactional
    public void logProjectDeleted(UUID userId, UUID projectId) {
        save(userId, projectId, null, AuditAction.DELETE, EntityType.PROJECT);
    }

    /*
     * =========================
     * TASK LEVEL
     * =========================
     */

    @Transactional
    public void logTaskCreated(UUID userId, UUID projectId, UUID taskId) {
        save(userId, projectId, taskId, AuditAction.CREATE, EntityType.TASK);
    }

    @Transactional
    public void logTaskUpdated(UUID userId, UUID projectId, UUID taskId) {
        save(userId, projectId, taskId, AuditAction.UPDATE, EntityType.TASK);
    }

    @Transactional
    public void logTaskDeleted(UUID userId, UUID projectId, UUID taskId) {
        save(userId, projectId, taskId, AuditAction.DELETE, EntityType.TASK);
    }

    /*
     * =========================
     * CORE SAVE (PRIVATE)
     * =========================
     */

    private void save(
            UUID userId,
            UUID projectId,
            UUID taskId,
            AuditAction action,
            EntityType entityType) {
        AuditLog log = new AuditLog();
        log.setUserId(userId);
        log.setProjectId(projectId);
        log.setTaskId(taskId);
        log.setAction(action);
        log.setEntityType(entityType);
        log.setCreatedAt(Instant.now());

        auditLogRepository.save(log);
    }
}
