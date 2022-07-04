package com.testco.intunewebapp.controller;

import com.testco.intunewebapp.service.ResourceService;
import com.testco.intunewebapp.service.VerifyGroupException;
import com.testco.intunewebapp.service.VerifyService;
import com.testco.intunewebapp.service.recieve.FileCheck;
import com.testco.intunewebapp.service.version.AppVersionException;
import com.testco.intunewebapp.service.version.AppVersionService;
import com.testco.iw.api.IntuneApi;
import com.testco.iw.models.Accepted;
import com.testco.iw.models.BadRequest;
import com.testco.iw.models.FileUpload;
import com.testco.iw.models.Forbidden;
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
    private final AppVersionService appVersionService;

    public IntuneController(VerifyService verifyService, ResourceService resourceService, FileCheck fileCheck, HttpServletRequest request, AppVersionService appVersionService) {
        this.verifyService = verifyService;
        this.resourceService = resourceService;
        this.fileCheck = fileCheck;
        this.request = request;
        this.appVersionService = appVersionService;
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
        if(!fileCheck.validUpload(body)){
            return new ResponseEntity(new BadRequest(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new Accepted(), HttpStatus.ACCEPTED);
    }

    @Override
    public ResponseEntity<Accepted> verify() {
        try {
            appVersionService.verifyVersion(request);
        } catch (AppVersionException e){
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
