package com.testco.intunewebapp.service.version;


import javax.servlet.http.HttpServletRequest;

@Deprecated
public interface VersionHeaderService {
    void verifyVersion(HttpServletRequest request);
}
