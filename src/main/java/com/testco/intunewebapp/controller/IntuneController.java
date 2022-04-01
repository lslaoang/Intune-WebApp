package com.testco.intunewebapp.controller;

import com.testco.intunewebapp.model.response.Accepted;
import com.testco.intunewebapp.model.response.UnAuthorizedAccess;
import com.testco.intunewebapp.service.VerifyGroupException;
import com.testco.intunewebapp.service.VerifyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${api.endpoints.base-path}")
public class IntuneController {

    private static final Logger LOGGER = LoggerFactory.getLogger(IntuneController.class);

    private final VerifyService verifyService;

    public IntuneController(VerifyService verifyService) {
        this.verifyService = verifyService;
    }

    @GetMapping(path = "/verify")
    public ResponseEntity<?> verify() {
        try {
            verifyService.authorize();
        } catch (VerifyGroupException e) {
            LOGGER.warn("Authorization check failed. {}", e.getMessage());
            return new ResponseEntity<>(new UnAuthorizedAccess(), HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(new Accepted(), HttpStatus.ACCEPTED);
    }

    @GetMapping(path = "/check")
    public ResponseEntity<?> check() {
        return new ResponseEntity<>("All Good!", HttpStatus.ACCEPTED);
    }
}
