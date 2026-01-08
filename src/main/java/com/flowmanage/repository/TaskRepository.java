package com.flowmanage.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.flowmanage.entity.Task;

public interface TaskRepository extends JpaRepository<Task, UUID> {
    Page<Task> findAllByProjectId(UUID projectId, Pageable pageable);

    Optional<Task> findByIdAndProjectId(UUID taskId, UUID projectId);

    boolean existsByIdAndProjectId(UUID taskId, UUID projectId);
}
