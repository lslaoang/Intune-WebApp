package com.testco.intunewebapp.service.resource;

import com.azure.spring.aad.AADOAuth2AuthenticatedPrincipal;
import com.testco.intunewebapp.service.resource.handler.ResourceResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.TrustAllStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;

import static org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction.clientRegistrationId;

@Service
public class ResourceService {

    private static final Logger LOGGER = Logger.getLogger(ResourceService.class.getName());
    private static final int REQUEST_TIMEOUT_MS = 5000;
    private static final int CONNECTION_TIMEOUT_MS = 5000;

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

//           //Will change the signed token if used in another authorized endpoint
//            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//            AADOAuth2AuthenticatedPrincipal user = (AADOAuth2AuthenticatedPrincipal) principal;

            LOGGER.info("Exchange token: \n" + token);

        } catch (Exception e) {
            LOGGER.error("Failed to check resource." + e);
            throw new RuntimeException("Error occurred calling resource endpoint.", e);
        }
    }

    public void checkResourceViaHttpClient() {
        LOGGER.info("Checking of resources via HTTP client started.");

        HttpPost request = new HttpPost(UriComponentsBuilder.fromHttpUrl(resourceBaseUri).path(resourceEndpoint).build().toUri());
        request.setHeader("Authorization", "Bearer "+ getUserToken());

        try {
            CloseableHttpClient client = HttpClients.custom()
                    .setSSLContext(new SSLContextBuilder()
                            .loadTrustMaterial(null, TrustAllStrategy.INSTANCE).build())
                    .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                    .setDefaultRequestConfig(RequestConfig.custom()
                            .setConnectionRequestTimeout(REQUEST_TIMEOUT_MS)
                            .setConnectTimeout(CONNECTION_TIMEOUT_MS)
                            .build())
                    .build();
            client.execute(request, ResourceResponseHandler.handleResponse);
            LOGGER.info("Retrieving of resources successful!");

        } catch (Exception e) {
            LOGGER.error("Failed to check resource." + e);
            throw new RuntimeException("Error occurred calling resource endpoint.", e);
        }
    }

    private String getUserToken(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        AADOAuth2AuthenticatedPrincipal user = (AADOAuth2AuthenticatedPrincipal) principal;
        return user.getTokenValue();
    }
}
