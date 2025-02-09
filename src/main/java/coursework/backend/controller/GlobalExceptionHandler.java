package coursework.backend.controller;

import coursework.backend.dto.ErrorResponse;
import coursework.backend.exception.ForbiddenException;
import coursework.backend.exception.NotFoundException;
import coursework.backend.exception.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.util.Arrays;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedException(UnauthorizedException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("Unauthorized", ex.getMessage()));
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorResponse> handleForbiddenException(ForbiddenException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorResponse("Forbidden", ex.getMessage()));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("Notfound", ex.getMessage()));
    }

    @ExceptionHandler({HandlerMethodValidationException.class, MethodArgumentNotValidException.class})
    public ResponseEntity<ErrorResponse> handleValidationException(Exception ex) {
        StringBuilder fieldErrors = new StringBuilder();

        if (ex instanceof MethodArgumentNotValidException validationEx) {
            validationEx.getBindingResult().getFieldErrors().forEach(fieldError ->
                    fieldErrors.append(fieldError.getField())
                            .append(": ")
                            .append(fieldError.getDefaultMessage())
                            .append("\n")
            );
        } else if (ex instanceof HandlerMethodValidationException validationEx) {
            validationEx.getParameterValidationResults().forEach(result ->
                    result.getResolvableErrors().forEach(error -> {
                        String field = (error.getCodes() != null && error.getCodes().length > 1)
                                ? error.getCodes()[1].split("\\.")[1]
                                : "unknownField";
                        System.out.println(Arrays.toString(error.getCodes()));
                        fieldErrors.append(field)
                                .append(": ")
                                .append(error.getDefaultMessage())
                                .append("\n");
                    })
            );
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Validation failed", fieldErrors.toString().trim()));
    }
}
