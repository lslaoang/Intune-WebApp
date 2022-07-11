package com.testco.intunewebapp.service.version;


import javax.servlet.http.HttpServletRequest;

public interface VersionHeaderService {
    void verifyVersion(HttpServletRequest request);
}
