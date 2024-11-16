package ma.yc.Citronix.common.infrastructure.web;

import java.time.LocalDateTime;

public record ErrorResponse(
        LocalDateTime timestamp,
        int status,
        String error,
        String message) {
}
