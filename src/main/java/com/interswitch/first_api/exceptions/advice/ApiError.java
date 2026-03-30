package com.interswitch.first_api.exceptions.advice;

import java.time.LocalDateTime;

public record ApiError(String path, String errorMessage, String errorTraceId, int statusCode, LocalDateTime timestamp) {
}
