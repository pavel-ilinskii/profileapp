package com.ilinskii.profileapp.exception;

public class ExecutionConflictException extends RuntimeException {

    public ExecutionConflictException() {
        super();
    }

    public ExecutionConflictException(String msg) {
        super(msg);
    }

    public ExecutionConflictException(String msg, Throwable t) {
        super(msg, t);
    }
}
