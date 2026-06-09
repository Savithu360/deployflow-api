package com.savithu.deployflow.exception;

import com.savithu.deployflow.dto.ApiErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(IncidentNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFound(
            IncidentNotFoundException exception, HttpServletRequest request) {
        return error(HttpStatus.NOT_FOUND, exception.getMessage(), request, Map.of());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidation(
            MethodArgumentNotValidException exception, HttpServletRequest request) {
        Map<String, String> errors = new LinkedHashMap<>();
        exception.getBindingResult().getFieldErrors()
                .forEach(error -> errors.putIfAbsent(error.getField(), error.getDefaultMessage()));
        return error(HttpStatus.BAD_REQUEST, "Validation failed", request, errors);
    }

    @ExceptionHandler({HttpMessageNotReadableException.class, MethodArgumentTypeMismatchException.class})
    public ResponseEntity<ApiErrorResponse> handleInvalidValue(Exception exception, HttpServletRequest request) {
        return error(HttpStatus.BAD_REQUEST,
                "Invalid request value. Use one of the documented enum values.", request, Map.of());
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleMissingRoute(
            NoResourceFoundException exception, HttpServletRequest request) {
        return error(HttpStatus.NOT_FOUND, "Resource not found", request, Map.of());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleUnexpected(Exception exception, HttpServletRequest request) {
        log.error("Unexpected error while processing {}", request.getRequestURI(), exception);
        return error(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected server error occurred", request, Map.of());
    }

    private ResponseEntity<ApiErrorResponse> error(
            HttpStatus status, String message, HttpServletRequest request, Map<String, String> errors) {
        ApiErrorResponse response = new ApiErrorResponse(
                LocalDateTime.now(), status.value(), status.getReasonPhrase(),
                message, request.getRequestURI(), errors
        );
        return ResponseEntity.status(status).body(response);
    }
}
