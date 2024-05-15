package com.testco.intunewebapp.service.version;


import jakarta.servlet.http.HttpServletRequest;

public interface VersionHeaderService {
    void verifyVersion(HttpServletRequest request);
}
