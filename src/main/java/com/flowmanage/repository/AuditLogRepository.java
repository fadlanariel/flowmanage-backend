package com.flowmanage.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.flowmanage.entity.AuditLog;

public interface AuditLogRepository
        extends JpaRepository<AuditLog, UUID>,
        JpaSpecificationExecutor<AuditLog> {
}
