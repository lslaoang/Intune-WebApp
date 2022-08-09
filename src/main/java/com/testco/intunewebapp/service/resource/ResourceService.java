package com.testco.intunewebapp.service.resource;

import com.azure.spring.aad.AADOAuth2AuthenticatedPrincipal;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;

import static org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction.clientRegistrationId;

@Service
public class ResourceService {

    private static final Logger LOGGER = Logger.getLogger(ResourceService.class.getName());

    private final WebClient webClient;
    private final HttpServletRequest servletRequest;

    @Value("${provider.apigee.base-uri}")
    String resourceBaseUri;

    @Value("${provider.apigee.verify}")
    String resourceEndpoint;

    public ResourceService(WebClient webClient, HttpServletRequest servletRequest) {
        this.webClient = webClient;
        this.servletRequest = servletRequest;
    }

    public void checkResource() {
        LOGGER.info("Checking of resources started.");

        try {
            String token = webClient
                    .get()
                    .uri(new URI(resourceBaseUri + resourceEndpoint))
//                    .attributes(clientRegistrationId("testco-res")) //When calling outside instances, specify the clientRegistrationId for the called endpoint
                    .attributes(clientRegistrationId("testco-webapp")) //Within app since Spring security will check the validity of the token for this instance
                    .header("X-WEBAPP-SOURCE", "INTERNAL")
                    .header("X-WEBAPP-ADDRESS", servletRequest.getRemoteAddr())
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            LOGGER.info("Retrieving of resources successful!");
            System.out.println("Is this it: " + token);

            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            AADOAuth2AuthenticatedPrincipal user = (AADOAuth2AuthenticatedPrincipal) principal;

        } catch (Exception e) {
            LOGGER.error("Failed to check resource." + e);
            throw new RuntimeException("Error occurred calling resource endpoint.", e);
        }
    }
}
