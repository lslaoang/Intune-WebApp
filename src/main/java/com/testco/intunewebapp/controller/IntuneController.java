package com.testco.intunewebapp.controller;

import com.testco.intunewebapp.service.ResourceService;
import com.testco.intunewebapp.service.VerifyService;
import com.testco.iw.api.VerifyApi;
import com.testco.iw.models.Accepted;
import com.testco.iw.models.Forbidden;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.base-path}")
public class IntuneController implements VerifyApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(IntuneController.class);

    private final VerifyService verifyService;
    private final ResourceService resourceService;

    public IntuneController(VerifyService verifyService, ResourceService resourceService) {
        this.verifyService = verifyService;
        this.resourceService = resourceService;
    }

    @Override
    public ResponseEntity<Accepted> verify() {
        try {
            resourceService.checkResource();
            verifyService.authorize();
        } catch (RuntimeException e) {
            LOGGER.warn("Authorization check failed. {}", e.getMessage());
            return new ResponseEntity(new Forbidden(), HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(new Accepted(), HttpStatus.ACCEPTED);
    }
}
