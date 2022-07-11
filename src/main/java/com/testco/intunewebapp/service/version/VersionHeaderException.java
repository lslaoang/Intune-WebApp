package com.testco.intunewebapp.service.version;

public class VersionHeaderException extends RuntimeException{
    public VersionHeaderException() {
        super();
    }

    public VersionHeaderException(String message) {
        super(message);
    }

    public VersionHeaderException(String message, Throwable cause) {
        super(message, cause);
    }
}
