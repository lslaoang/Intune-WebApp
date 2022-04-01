package com.testco.intunewebapp.service;

public class VerifyGroupException extends RuntimeException{
    public VerifyGroupException() {
        super();
    }

    public VerifyGroupException(String message) {
        super(message);
    }

    public VerifyGroupException(String message, Throwable cause) {
        super(message, cause);
    }
}
