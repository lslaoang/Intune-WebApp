package com.testco.intunewebapp.service.verify;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URI;
import java.net.URISyntaxException;

import static com.testco.intunewebapp.util.RequestUtil.getUserNameFromRequest;
import static org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction.clientRegistrationId;

@Service
public class VerifyServiceImpl implements VerifyService {

    private static final Logger LOGGER = LoggerFactory.getLogger(VerifyService.class);
    private static final String GRAPH_ENDPOINT = "https://graph.microsoft.com/v1.0/";

    private final WebClient webClient;

    @Value("${app.allowed-group}")
    private String allowedGroup;

    public VerifyServiceImpl(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public void authorize() {
        LOGGER.info("Checking authorization.");

        String userEmail = getUserNameFromRequest();
        if (userEmail == null) {
            throw new VerifyGroupException("Claim \"preferred_username\" with user email is not present in the token. ");
        }
        String groupEncoded = allowedGroup.replace(" ", "%20");
        String memberOfEndpoint = GRAPH_ENDPOINT
                + "users/" + userEmail
                + "/memberOf/microsoft.graph.group?$count=true&$filter=displayName%20eq%20'"
                + groupEncoded + "'";

        URI uri;
        try {
            uri = new URI(memberOfEndpoint);
        } catch (URISyntaxException e) {
            throw new VerifyGroupException(
                    "Unable to create Member of URI from link: . " + memberOfEndpoint + "\n" + e.getMessage()
            );
        }

        String graphResponse;
        try {
            graphResponse = webClient
                    .get()
                    .uri(uri)
                    .attributes(clientRegistrationId("graph"))
                    .header("ConsistencyLevel", "eventual")
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        } catch (Exception e) {
            throw new VerifyGroupException("Failed to call Graph API. " + e.getMessage());
        }

        if (graphResponse == null) {
            throw new VerifyGroupException("Failed verify Group from Graph API assignment.");
        }

        JSONObject jsonObject = new JSONObject(graphResponse);
        Integer groupCount = (Integer) jsonObject.get("@odata.count");
        if (groupCount != 1) {
            throw new VerifyGroupException(String.format("No group found for the user %s.", userEmail));
        }
        LOGGER.info("Authorization check passed.");
    }
}
