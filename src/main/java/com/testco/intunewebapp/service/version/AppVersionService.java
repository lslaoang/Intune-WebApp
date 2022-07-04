package com.testco.intunewebapp.service.version;


import javax.servlet.http.HttpServletRequest;

public interface AppVersionService {
    void verifyVersion(HttpServletRequest request);
}
