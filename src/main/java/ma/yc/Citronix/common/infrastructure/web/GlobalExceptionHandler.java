package ma.yc.Citronix.common.infrastructure.web;

import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

}

record ErrorResponse(
        LocalDateTime timestamp,
        int status,
        String error,
        String message) {
}