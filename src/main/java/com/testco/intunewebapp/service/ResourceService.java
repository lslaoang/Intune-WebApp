package com.testco.intunewebapp.service;

import com.azure.spring.aad.AADOAuth2AuthenticatedPrincipal;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import static org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction.clientRegistrationId;

@Service
public class ResourceService {

    private static final Logger LOGGER = Logger.getLogger(ResourceService.class.getName());

    private final WebClient webClient;
    @Value("${api.provider.resource.base-uri}")
    String resourceBaseUri;
    @Value("${api.provider.resource.endpoint}")
    String resourceEndpoint;

    @Value("${server.port}")
    String hostBaseUrl;

    public ResourceService(WebClient webClient) {
        this.webClient = webClient;
    }

    public void checkResource() {
        LOGGER.info("Checking of resources started.");

        try {
            String token = webClient
                    .get()
//                    .uri(new URI(resourceBaseUri + resourceEndpoint))
                    .uri("http://localhost:" + hostBaseUrl + "/apigee")
                    .attributes(clientRegistrationId("testco-webapp"))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            LOGGER.info("Retrieving of resources successful!");
            System.out.println("Is this it: " + token);

            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            AADOAuth2AuthenticatedPrincipal user = (AADOAuth2AuthenticatedPrincipal) principal;
            System.out.println(user.getTokenValue());

        } catch (Exception e) {
            LOGGER.error("Failed to check resource." + e);
            throw new RuntimeException("Error occurred calling resource endpoint.", e);
        }
    }
}
