package com.testco.intunewebapp.service;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URI;

import static org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction.oauth2AuthorizedClient;

@Service
public class ResourceService {

    private static final Logger LOGGER = Logger.getLogger(ResourceService.class.getName());

    private final WebClient webClient;

    @Value("${api.provider.resource.base-uri}")
    String resourceBaseUri;

    @Value("${api.provider.resource.endpoint}")
    String resourceEndpoint;

    public ResourceService(WebClient webClient) {
        this.webClient = webClient;
    }

    public void checkResource(OAuth2AuthorizedClient authorizedClient) {
        LOGGER.info("Checking of resources started.");
        try {
            webClient
                    .get()
                    .uri(new URI(resourceBaseUri + resourceEndpoint))
                    .attributes(oauth2AuthorizedClient(authorizedClient))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            LOGGER.info("Retrieving of resources successful!");
        } catch (Exception e) {
            LOGGER.error("Failed to check resource.");
            throw new RuntimeException("Error occurred calling resource endpoint.", e);
        }
    }
}
