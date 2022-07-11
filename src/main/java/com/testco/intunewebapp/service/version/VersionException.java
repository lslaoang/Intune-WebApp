package com.testco.intunewebapp.service.version;

public class VersionException extends RuntimeException{
    public VersionException() {
        super();
    }

    public VersionException(String message) {
        super(message);
    }

    public VersionException(String message, Throwable cause) {
        super(message, cause);
    }
}
