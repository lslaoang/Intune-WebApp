package com.testco.intunewebapp.config.handler;


import com.testco.iw.models.Forbidden;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

import static com.testco.intunewebapp.util.ResponseWriterUtil.writeErrorResponse;

@Component
public class IntuneAuthenticationFailureFilter {

    private static final Logger LOGGER = Logger.getLogger(IntuneAuthenticationFailureFilter.class.getName());

    public BearerTokenAuthenticationFilter bearerTokenAuthenticationFilter(AuthenticationManager authenticationManager) {
        BearerTokenAuthenticationFilter filter = new BearerTokenAuthenticationFilter(authenticationManager);
        filter.setAuthenticationFailureHandler(authenticationFailureHandler());
        return filter;
    }

    private AuthenticationFailureHandler authenticationFailureHandler() {
        return (((request, response, exception) -> {
            LOGGER.severe(request.getHeader("Authorization"));
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            writeErrorResponse(response, new Forbidden());
            LOGGER.severe(String.format("Access from %s denied. Token invalid. ", request.getRemoteAddr()) + exception.getMessage());
       }));
    }
}
