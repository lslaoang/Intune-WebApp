package com.testco.intunewebapp.service.version;

public class AppVersionException extends RuntimeException{
    public AppVersionException() {
        super();
    }

    public AppVersionException(String message) {
        super(message);
    }

    public AppVersionException(String message, Throwable cause) {
        super(message, cause);
    }
}
