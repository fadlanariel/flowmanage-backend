CREATE TABLE audit_logs (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    project_id UUID NOT NULL,
    task_id UUID,
    action VARCHAR(20) NOT NULL,
    entity_type VARCHAR(100) NOT NULL,
    created_at TIMESTAMP NOT NULL
);

CREATE INDEX idx_audit_project ON audit_logs(project_id);
CREATE INDEX idx_audit_task ON audit_logs(task_id);
CREATE INDEX idx_audit_created ON audit_logs(created_at);
