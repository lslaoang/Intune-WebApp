package com.testco.intunewebapp.controller;

import com.google.gson.Gson;
import com.testco.intunewebapp.model.UploadRequest;
import com.testco.intunewebapp.model.UploadRequestRaw;
import com.testco.iw.models.Accepted;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class ApigeeController {

    static final String UBS_INTERNAL = "X-WEBAPP-SOURCE";
    static final String UBS_INTERNAL_VALUE = "INTERNAL";

    private final HttpServletRequest request;

    public ApigeeController(HttpServletRequest request) {
        this.request = request;
    }

    @GetMapping("/api/v1/verify")
    public String getExchangeToken(@RegisteredOAuth2AuthorizedClient("testco-res") OAuth2AuthorizedClient oAuth2AuthorizedClient) {

        if (isInternalRequest()) {
            return oAuth2AuthorizedClient.getAccessToken().getTokenValue();
        } else {
            throw new RuntimeException("Required attribute not found.");
        }
    }

    @PostMapping("/api/v1/file-upload")
    public ResponseEntity<Accepted> uploadFile(@RequestBody UploadRequest UploadRequest) {
        System.out.println("Full Request: "  + new Gson().toJson(UploadRequest));
        return new ResponseEntity<>(new Accepted(), HttpStatus.ACCEPTED);

    }

    private Boolean isInternalRequest() {
        return request.getHeader(UBS_INTERNAL).equals(UBS_INTERNAL_VALUE);
    }
}
