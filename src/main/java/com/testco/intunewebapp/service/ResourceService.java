package com.testco.intunewebapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URI;
import java.net.URISyntaxException;

import static org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction.oauth2AuthorizedClient;

@Service
public class ResourceService {

    @Value("${api.endpoints.resource}")
    String resourceLink;

    @Autowired
    WebClient webClient;

    public void checkResource(OAuth2AuthorizedClient authorizedClient){

        URI uri;
        try {
            uri = new URI(resourceLink);
        } catch (
                URISyntaxException e) {
            throw new RuntimeException("Unable to create URI. {}", e);
        }
        System.out.println(authorizedClient.getAccessToken().getTokenValue());

        try {
            String response = webClient
                    .get()
                    .uri(uri)
                    .attributes(oauth2AuthorizedClient(authorizedClient))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            System.out.println("Success!" + response);
        } catch (Exception e) {
            System.out.println("Failed to check resource.");
            throw new RuntimeException("Error occurred calling resource endpoint.", e);
        }
    }
}
