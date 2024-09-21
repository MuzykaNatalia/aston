package ru.aston.exception;

import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

import static ru.aston.constant.Constant.FORMATTER;

@Getter
@ToString
public class ErrorResponse {
    private final String timestamp;
    private final HttpStatus status;
    private final String reason;
    private final String message;

    public ErrorResponse(HttpStatus status, String reason, String message) {
        this.timestamp = FORMATTER.format(LocalDateTime.now());
        this.status = status;
        this.reason = reason;
        this.message = message;
    }
}
