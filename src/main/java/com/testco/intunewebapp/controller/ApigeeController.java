package com.testco.intunewebapp.controller;

import com.testco.intunewebapp.config.ApigeeConfig;
import com.testco.intunewebapp.model.UploadRequest;
import com.testco.iw.models.Accepted;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import static com.testco.intunewebapp.util.RequestUtil.printObjectAsString;

@RestController
public class ApigeeController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ApigeeController.class);

    static final String UBS_INTERNAL = "X-WEBAPP-SOURCE";
    static final String UBS_INTERNAL_VALUE = "INTERNAL";

    private final HttpServletRequest request;
    private final ApigeeConfig apigeeConfig;

    public ApigeeController(HttpServletRequest request, ApigeeConfig apigeeConfig) {
        this.request = request;
        this.apigeeConfig = apigeeConfig;
    }


    @GetMapping("/api/v1/verify")
    public String getExchangeToken(@RegisteredOAuth2AuthorizedClient("testco-res") OAuth2AuthorizedClient oAuth2AuthorizedClient) throws InterruptedException {

        if (isInternalRequest()) {
            System.out.println("You've reached this.");
//            Thread.sleep(15010);
            return oAuth2AuthorizedClient.getAccessToken().getTokenValue();
        } else {
            throw new RuntimeException("Required attribute not found.");
        }
    }

    @PostMapping("/api/v1/verify")
    public ResponseEntity verifyResource(@RegisteredOAuth2AuthorizedClient("testco-res") OAuth2AuthorizedClient oAuth2AuthorizedClient) throws InterruptedException {
            System.out.println("You've reached this.");
            String name = oAuth2AuthorizedClient.getPrincipalName();
            return new ResponseEntity<>("You got here! " +  name ,HttpStatus.ACCEPTED);
    }

    @PostMapping("/api/v1/file-upload")
    public ResponseEntity<Accepted> uploadFile( UploadRequest uploadRequest) {
        printObjectAsString(uploadRequest.getUploadMetadata());
        return new ResponseEntity<>(new Accepted(), HttpStatus.ACCEPTED);

    }

    private Boolean isInternalRequest() {
        return request.getHeader(UBS_INTERNAL).equals(UBS_INTERNAL_VALUE);
    }
}
