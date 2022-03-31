package com.testco.intunewebapp.config;

import com.testco.intunewebapp.model.response.UnAuthorizedAccess;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.logging.Logger;

import static com.testco.intunewebapp.util.ResponseWriterUtil.writeErrorResponse;

@Component
public class IntuneAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final Logger LOGGER = Logger.getLogger(IntuneAuthenticationEntryPoint.class.getName());

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) {

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        writeErrorResponse(response, new UnAuthorizedAccess());
        LOGGER.severe(String.format("Access from %s denied.", request.getRemoteAddr()));

    }
}
