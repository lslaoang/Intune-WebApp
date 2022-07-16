package com.testco.intunewebapp.controller;

import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.net.UnknownHostException;

@RestController
public class ApigeeController {

    static final String UBS_INTERNAL = "X-WEBAPP-SOURCE";
    static final String UBS_INTERNAL_VALUE = "INTERNAL";
    
    private final HttpServletRequest request;

    public ApigeeController(HttpServletRequest request) {
        this.request = request;
    }

    @GetMapping("/apigee")
    public String getExchangeToken(@RegisteredOAuth2AuthorizedClient("testco-res") OAuth2AuthorizedClient oAuth2AuthorizedClient) throws UnknownHostException {

        if (isInternalRequest()) {
            return oAuth2AuthorizedClient.getAccessToken().getTokenValue();
        } else {
            throw new RuntimeException("Required attribute not found.");
        }

    }

    private Boolean isInternalRequest() throws UnknownHostException {
//        String hostAddress = String.valueOf(InetAddress.getLocalHost().getHostAddress());
//        String requestAddress = request.getLocalAddr();
        return request.getHeader(UBS_INTERNAL).equals(UBS_INTERNAL_VALUE);
    }
}
