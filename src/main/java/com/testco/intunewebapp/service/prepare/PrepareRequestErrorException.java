package com.testco.intunewebapp.service.prepare;

public class PrepareRequestErrorException extends RuntimeException{
    public PrepareRequestErrorException() {
    }
    public PrepareRequestErrorException(String message) {
        super(message);
    }
}
