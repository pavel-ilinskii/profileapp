package com.ilinskii.profileapp.exception;

import lombok.Getter;
import org.springframework.validation.ObjectError;

import java.util.List;

@Getter
public class ValidationException extends RuntimeException {

    private List<ObjectError> errors;

    public ValidationException() {
        super();
    }

    public ValidationException(List<ObjectError> errors) {
        super("Validation fails");
        this.errors = errors;
    }
}
