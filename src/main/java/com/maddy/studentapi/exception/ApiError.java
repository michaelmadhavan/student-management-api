package com.maddy.studentapi.exception;

import java.time.LocalDateTime;
import java.util.Map;

/** Standard error body returned by the API. */
public record ApiError(LocalDateTime timestamp, int status, String message, Map<String, String> errors) {
}
