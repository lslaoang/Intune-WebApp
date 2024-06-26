package com.testco.intunewebapp.handler;


import com.testco.iw.models.AccessDenied;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;


import java.io.IOException;
import java.util.logging.Logger;

import static com.testco.intunewebapp.util.ResponseWriterUtil.writeErrorResponse;

@Component
public class IntuneAuthenticationFailureFilter implements AuthenticationFailureHandler{

    private static final Logger LOGGER = Logger.getLogger(IntuneAuthenticationFailureFilter.class.getName());

    public BearerTokenAuthenticationFilter bearerTokenAuthenticationFilter(AuthenticationManager authenticationManager) {
        LOGGER.severe("Checking request ...");
        BearerTokenAuthenticationFilter filter = new BearerTokenAuthenticationFilter(authenticationManager);
        filter.setAuthenticationFailureHandler(authenticationFailureHandler());
        return filter;
    }

    private AuthenticationFailureHandler authenticationFailureHandler() {
        return (((request, response, exception) -> {
            LOGGER.severe(request.getHeader("Authorization"));
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            if (exception.getMessage().contains("Jwt expired")) {
                writeErrorResponse(response, new AccessDenied().code(4012));
                LOGGER.severe(String.format("Access from %s denied. Token expired. ", request.getRemoteAddr()) + exception.getMessage());
            } else {
                writeErrorResponse(response, new AccessDenied().code(4011));
                LOGGER.severe(String.format("Access from %s denied. Token invalid. ", request.getRemoteAddr()) + exception.getMessage());
            }

        }));
    }

    /**
     * Called when an authentication attempt fails.
     *
     * @param request   the request during which the authentication attempt occurred.
     * @param response  the response.
     * @param exception the exception which was thrown to reject the authentication
     */
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        LOGGER.severe("you have reach this ...");
    }
}
