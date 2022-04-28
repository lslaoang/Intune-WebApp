package com.testco.intunewebapp.controller;

import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApigeeController {

    @GetMapping("/apigee")
    public String getExchangeToken(@RegisteredOAuth2AuthorizedClient("testco-res")OAuth2AuthorizedClient oAuth2AuthorizedClient){
        System.out.println("This is the token: " + oAuth2AuthorizedClient.getAccessToken().getTokenValue());
        return oAuth2AuthorizedClient.getAccessToken().getTokenValue();
    }
}
