package com.ilinskii.profileapp.api;

import com.ilinskii.profileapp.exception.ExecutionConflictException;
import com.ilinskii.profileapp.exception.NotFoundObjectException;
import com.ilinskii.profileapp.exception.ValidationException;
import com.ilinskii.profileapp.service.ErrorLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final ErrorLogService errorLogService;

    @ExceptionHandler(value = ValidationException.class)
    public ResponseEntity<ErrorMsg> handleValidationException(ValidationException e) {
        StringBuilder errorMessage = new StringBuilder();

        if (null != e.getErrors()) {
            e.getErrors().forEach(error -> errorMessage.append(error.getDefaultMessage() + ";"));
        }
        String msg = errorMessage.toString();
        logError(msg);

        return ResponseEntity.badRequest().body(new ErrorMsg(msg));
    }

    @ExceptionHandler(value = NotFoundObjectException.class)
    public ResponseEntity<ErrorMsg> handleNotFoundObjectException(NotFoundObjectException e) {
        String msg = e.getMessage();
        logError(msg);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorMsg(msg));
    }

    @ExceptionHandler(value = ExecutionConflictException.class)
    public ResponseEntity<ErrorMsg> handleExecutionConflictException(ExecutionConflictException e) {
        String msg = e.getMessage();
        logError(msg);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorMsg(msg));
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ErrorMsg> handleException(Exception e) {
        String msg = e.getMessage();
        logError(msg);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorMsg(msg));
    }

    private void logError(String msg) {
        errorLogService.logError(msg);
    }
}
