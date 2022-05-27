package com.testco.intunewebapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;

@RestController
public class ApigeeController {

    static final String UBS_INTERNAL = "X-WEBAPP-SOURCE";
    static final String UBS_INTERNAL_VALUE = "INTERNAL";

    @Autowired
    private HttpServletRequest request;

    @GetMapping("/apigee")
    public String getExchangeToken(@RegisteredOAuth2AuthorizedClient("testco-res") OAuth2AuthorizedClient oAuth2AuthorizedClient) throws UnknownHostException {

        if (isInternalRequest()) {
            return oAuth2AuthorizedClient.getAccessToken().getTokenValue();
        } else {
            throw new RuntimeException("Required attribute not found.");
        }

    }

    private Boolean isInternalRequest() throws UnknownHostException {
        String hostAddress = String.valueOf(InetAddress.getLocalHost().getHostAddress());
        System.out.println(hostAddress);
        String requestAddress = request.getLocalAddr();
        System.out.println(requestAddress);
        System.out.println("Address header: " + request.getHeader("X-WEBAPP-ADDRESS")) ;
        return request.getHeader(UBS_INTERNAL).equals(UBS_INTERNAL_VALUE);
    }
}
