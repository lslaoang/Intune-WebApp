package com.testco.intunewebapp.controller;

import com.testco.intunewebapp.service.ResourceService;
import com.testco.intunewebapp.service.VerifyGroupException;
import com.testco.intunewebapp.service.VerifyService;
import com.testco.intunewebapp.service.recieve.FileCheck;
import com.testco.intunewebapp.service.version.VersionBodyService;
import com.testco.intunewebapp.service.version.VersionException;
import com.testco.intunewebapp.service.version.VersionHeaderService;
import com.testco.iw.api.IntuneApi;
import com.testco.iw.models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("${api.base-path}")
public class IntuneController implements IntuneApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(IntuneController.class);

    private final VerifyService verifyService;
    private final ResourceService resourceService;
    private final FileCheck fileCheck;
    private final HttpServletRequest request;
    private final VersionHeaderService versionHeaderService;
    private final VersionBodyService versionBodyService;

    public IntuneController(VerifyService verifyService, ResourceService resourceService, FileCheck fileCheck, HttpServletRequest request, VersionHeaderService versionHeaderService, VersionBodyService versionBodyService) {
        this.verifyService = verifyService;
        this.resourceService = resourceService;
        this.fileCheck = fileCheck;
        this.request = request;
        this.versionHeaderService = versionHeaderService;
        this.versionBodyService = versionBodyService;
    }

    @Override
    public ResponseEntity<Accepted> uploadFile(FileUpload body) {
        try {
            resourceService.checkResource();
            verifyService.authorize();
        } catch (VerifyGroupException e) {
            LOGGER.warn("Authorization check failed. {}", e.getMessage());
            return new ResponseEntity(new Forbidden(), HttpStatus.FORBIDDEN);
        }
        if (!fileCheck.validUpload(body)) {
            return new ResponseEntity(new BadRequest(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new Accepted(), HttpStatus.ACCEPTED);
    }

    @Override
    public ResponseEntity<Accepted> verify(Verify body) {
        try {
            versionBodyService.verifyVersion(body.getAppOs(), body.getAppVersion());
        } catch (VersionException e) {
            NotSupported notSupported = new NotSupported();
            notSupported.setTitle("Bad request. " + e.getMessage());
            return new ResponseEntity(notSupported, HttpStatus.valueOf(402));
        }

        try {
            verifyService.authorize();
        } catch (VerifyGroupException e) {
            LOGGER.warn("Authorization check failed. {}", e.getMessage());
            return new ResponseEntity(new Forbidden(), HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(new Accepted(), HttpStatus.ACCEPTED);
    }

    @Deprecated
    public ResponseEntity<Accepted> verify() {
        try {
            versionHeaderService.verifyVersion(request);
        } catch (VersionException e) {
            BadRequest badRequest = new BadRequest();
            badRequest.setTitle("Bad request. " + e.getMessage());
            return new ResponseEntity(badRequest, HttpStatus.BAD_REQUEST);
        }

        try {
            verifyService.authorize();
        } catch (VerifyGroupException e) {
            LOGGER.warn("Authorization check failed. {}", e.getMessage());
            return new ResponseEntity(new Forbidden(), HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(new Accepted(), HttpStatus.ACCEPTED);
    }

}
