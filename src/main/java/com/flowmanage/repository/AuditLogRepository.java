package com.flowmanage.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.*;

import com.flowmanage.entity.AuditAction;
import com.flowmanage.entity.AuditLog;

public interface AuditLogRepository extends JpaRepository<AuditLog, UUID> {

    Page<AuditLog> findByUserId(UUID userId, Pageable pageable);

    Page<AuditLog> findByProjectId(UUID projectId, Pageable pageable);

    Page<AuditLog> findByTaskId(UUID taskId, Pageable pageable);

    Page<AuditLog> findByAction(AuditAction action, Pageable pageable);
}