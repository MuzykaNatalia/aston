package ru.aston.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;

import static ru.aston.constant.Constant.*;

@ControllerAdvice
@Slf4j
public class GlobalExceptionControllerAdvice {
    @ExceptionHandler(NotFoundException.class)
    public Object handleNotFoundExceptions(final NotFoundException e, HttpServletRequest request, Model model) {
        log.error("An not found error occurred: {}", e.getMessage(), e);

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.NOT_FOUND,
                REASON_NOT_FOUND,
                e.getMessage());

        if (isJsonRequest(request)) {
            return new ResponseEntity<>(errorResponse, new HttpHeaders(), errorResponse.getStatus());
        } else {
            model.addAttribute("errorResponse", errorResponse);
            return "error";
        }
    }

    @ExceptionHandler(ConflictException.class)
    public Object handleConflictExceptions(final ConflictException e, HttpServletRequest request, Model model) {
        log.error("An conflict error occurred: {}", e.getMessage(), e);

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.CONFLICT,
                REASON_CONFLICT,
                e.getMessage());

        if (isJsonRequest(request)) {
            return new ResponseEntity<>(errorResponse, new HttpHeaders(), errorResponse.getStatus());
        } else {
            model.addAttribute("errorResponse", errorResponse);
            return "error";
        }
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public Object handleDataIntegrityViolationExceptions(final DataIntegrityViolationException e, HttpServletRequest request, Model model) {
        log.error("An DataIntegrityViolation error occurred: {}", e.getMessage(), e);

        String errorMessage = e.getMostSpecificCause().getMessage();

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.CONFLICT,
                REASON_CONFLICT,
                errorMessage);

        if (isJsonRequest(request)) {
            return new ResponseEntity<>(errorResponse, new HttpHeaders(), errorResponse.getStatus());
        } else {
            model.addAttribute("errorResponse", errorResponse);
            return "error";
        }
    }

    @ExceptionHandler(ValidationException.class)
    public Object handleValidationExceptions(final ValidationException e, HttpServletRequest request, Model model) {
        log.error("An validation error occurred: {}", e.getMessage(), e);

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST,
                REASON_BAD_REQUEST,
                e.getMessage());

        if (isJsonRequest(request)) {
            return new ResponseEntity<>(errorResponse, new HttpHeaders(), errorResponse.getStatus());
        } else {
            model.addAttribute("errorResponse", errorResponse);
            return "error";
        }
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public Object handleConstraintViolationExceptions(final ConstraintViolationException e, HttpServletRequest request, Model model) {
        log.error("An bad request error occurred: {}", e.getMessage(), e);

        List<String> errors = e.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessageTemplate).toList();

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST,
                REASON_BAD_REQUEST,
                errors.toString());

        if (isJsonRequest(request)) {
            return new ResponseEntity<>(errorResponse, new HttpHeaders(), errorResponse.getStatus());
        } else {
            model.addAttribute("errorResponse", errorResponse);
            return "error";
        }
    }

    @ExceptionHandler
    public Object handleAllExceptions(final Exception e, HttpServletRequest request, Model model) {
        log.error("An unexpected error occurred: {}", e.getMessage(), e);

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                REASON_INTERNAL_SERVER_ERROR,
                "An unexpected error occurred.");

        if (isJsonRequest(request)) {
            return new ResponseEntity<>(errorResponse, new HttpHeaders(), errorResponse.getStatus());
        } else {
            model.addAttribute("errorResponse", errorResponse);
            return "error";
        }
    }

    private boolean isJsonRequest(HttpServletRequest request) {
        String acceptHeader = request.getHeader("Accept");
        return acceptHeader != null && acceptHeader.contains("application/json");
    }
}
