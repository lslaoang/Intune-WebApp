package com.testco.intunewebapp.service.version;


import javax.servlet.http.HttpServletRequest;

public interface AppVersionService {
    boolean isCorrectVersion(HttpServletRequest request);
}
