package com.flowmanage.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.flowmanage.dto.response.ApiErrorResponse;

import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

        // =========================
        // 400 - Validation Error
        // =========================
        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ApiErrorResponse> handleValidationException(
                        MethodArgumentNotValidException ex,
                        HttpServletRequest request) {

                List<String> errors = ex.getBindingResult()
                                .getFieldErrors()
                                .stream()
                                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                                .toList();

                return ResponseEntity.badRequest().body(
                                new ApiErrorResponse(
                                                400,
                                                "VALIDATION_ERROR",
                                                "Request validation failed",
                                                request.getRequestURI(),
                                                errors));
        }

        // =========================
        // 400 - Business Bad Request
        // =========================
        @ExceptionHandler(BadRequestException.class)
        public ResponseEntity<ApiErrorResponse> handleBadRequest(
                        BadRequestException ex,
                        HttpServletRequest request) {
                return ResponseEntity.badRequest().body(
                                new ApiErrorResponse(
                                                400,
                                                "BAD_REQUEST",
                                                ex.getMessage(),
                                                request.getRequestURI(),
                                                null));
        }

        // =========================
        // 404 - Not Found
        // =========================
        @ExceptionHandler(ProjectNotFoundException.class)
        public ResponseEntity<ApiErrorResponse> handleNotFound(
                        ProjectNotFoundException ex,
                        HttpServletRequest request) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                                new ApiErrorResponse(
                                                404,
                                                "PROJECT_NOT_FOUND",
                                                ex.getMessage(),
                                                request.getRequestURI(),
                                                null));
        }

        // =========================
        // 403 - Forbidden (ownership)
        // =========================
        @ExceptionHandler(ProjectForbiddenException.class)
        public ResponseEntity<ApiErrorResponse> handleForbidden(
                        ProjectForbiddenException ex,
                        HttpServletRequest request) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                                new ApiErrorResponse(
                                                403,
                                                "PROJECT_FORBIDDEN",
                                                ex.getMessage(),
                                                request.getRequestURI(),
                                                null));
        }

        // =========================
        // 500 - Safety Net (LAST!)
        // =========================
        @ExceptionHandler(Exception.class)
        public ResponseEntity<ApiErrorResponse> handleUnexpected(
                        Exception ex,
                        HttpServletRequest request) {
                return ResponseEntity.internalServerError().body(
                                new ApiErrorResponse(
                                                500,
                                                "INTERNAL_SERVER_ERROR",
                                                "Unexpected server error",
                                                request.getRequestURI(),
                                                null));
        }
}
