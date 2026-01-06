package com.flowmanage.exception;

public class ProjectForbiddenException extends RuntimeException {
    public ProjectForbiddenException() {
        super("You are not allowed to access this project");
    }
}
