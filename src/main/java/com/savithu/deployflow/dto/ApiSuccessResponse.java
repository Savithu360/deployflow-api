package com.savithu.deployflow.dto;

import java.time.LocalDateTime;

public record ApiSuccessResponse(LocalDateTime timestamp, int status, String message) {
}
