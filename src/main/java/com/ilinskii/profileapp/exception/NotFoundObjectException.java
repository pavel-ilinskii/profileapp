package com.ilinskii.profileapp.exception;

public class NotFoundObjectException extends RuntimeException {

    public NotFoundObjectException() {
        super();
    }

    public NotFoundObjectException(String msg) {
        super(msg);
    }
}
