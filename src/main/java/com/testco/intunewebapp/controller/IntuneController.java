package com.testco.intunewebapp.controller;

import com.testco.intunewebapp.service.prepare.PrepareRequestErrorException;
import com.testco.intunewebapp.service.recieve.FileCheck;
import com.testco.intunewebapp.service.resource.ResourceService;
import com.testco.intunewebapp.service.upload.FileUploadService;
import com.testco.intunewebapp.service.upload.UploadErrorException;
import com.testco.intunewebapp.service.verify.VerifyGroupException;
import com.testco.intunewebapp.service.verify.VerifyService;
import com.testco.intunewebapp.service.version.CompatibilityService;
import com.testco.intunewebapp.service.version.VersionBodyService;
import com.testco.intunewebapp.service.version.VersionException;
import com.testco.intunewebapp.service.version.VersionHeaderService;
import com.testco.iw.api.IntuneApi;
import com.testco.iw.models.InternalError;
import com.testco.iw.models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("${api.base-path}")
public class IntuneController implements IntuneApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(IntuneController.class);

    private final VerifyService verifyService;
    private final VersionBodyService versionBodyService;
    private final ResourceService resourceService;
    private final FileCheck fileCheck;
    private final HttpServletRequest request;
    private final VersionHeaderService versionHeaderService;
    private final FileUploadService fileUploadService;
    private final CompatibilityService compatibilityService;

    public IntuneController(VerifyService verifyService, VersionBodyService versionBodyService,
                            ResourceService resourceService, FileCheck fileCheck,
                            HttpServletRequest request, VersionHeaderService versionHeaderService,
                            FileUploadService fileUploadService,
                            CompatibilityService compatibilityService) {
        this.verifyService = verifyService;
        this.versionBodyService = versionBodyService;
        this.resourceService = resourceService;
        this.fileCheck = fileCheck;
        this.request = request;
        this.versionHeaderService = versionHeaderService;
        this.fileUploadService = fileUploadService;
        this.compatibilityService = compatibilityService;
    }

    @Deprecated
    public ResponseEntity<Accepted> appVersionCheck(AppInformation body) {
        try {
            compatibilityService.verifyVersion(body);
        } catch (VersionException e){
            NotSupported notSupported = new NotSupported();
            notSupported.setTitle("Application not supported. " + e.getMessage());
            return new ResponseEntity(notSupported, HttpStatus.valueOf(402));
        }
        return new ResponseEntity<>(new Accepted(), HttpStatus.ACCEPTED);
    }

    @Value("${application.entity}")
    private List<String> appEntity;

    @Override
    public ResponseEntity<Accepted> appVersionCheck(AppInformation body, String X_UBS_APP_ENTITY) {
        try {
            if(! appEntity.contains(X_UBS_APP_ENTITY.toUpperCase(Locale.ROOT))){
                throw new VersionException("Expected header missing.");
            }
            compatibilityService.verifyVersion(body);

        } catch (VersionException e){
            NotSupported notSupported = new NotSupported();
            notSupported.setTitle("Error occurred while checking app version. " + e.getMessage());
            return new ResponseEntity(notSupported, HttpStatus.UPGRADE_REQUIRED);
        }
        return new ResponseEntity<>(new Accepted(), HttpStatus.ACCEPTED);
    }

    @Value("${application.version.backend}")
    private String backEndVersion;

    @Override
    public ResponseEntity<AppVersion> getVersion() {
        try{
            AppVersion appVersion  = new AppVersion();
            appVersion.setVersion(backEndVersion);
            return new ResponseEntity(appVersion, HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity(new InternalError(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<UploadSuccess> uploadFile(FileUpload fileUpload) {
        try {
//            resourceService.checkResource();
            verifyService.authorize();
        } catch (VerifyGroupException e) {
            LOGGER.warn("Authorization check failed. {}", e.getMessage());
            return new ResponseEntity(new Forbidden(), HttpStatus.FORBIDDEN);
        }

        if (!fileCheck.validUpload(fileUpload)) {
            return new ResponseEntity(new BadRequest(), HttpStatus.BAD_REQUEST);
        }

        try {
            fileUploadService.uploadToResource(fileUpload);
        } catch (UploadErrorException | PrepareRequestErrorException e) {
            return new ResponseEntity(new InternalError(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(new UploadSuccess(), HttpStatus.CREATED);
    }

    @Deprecated
    public ResponseEntity<Accepted> verify(AppInformation body) {
        try {
            compatibilityService.verifyVersion(body);
        } catch (VersionException e) {
            NotSupported notSupported = new NotSupported();
            notSupported.setTitle("Application not supported. " + e.getMessage());
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

    @Override
    public ResponseEntity<Accepted> verify() {

        try {
            resourceService.checkResource();
            verifyService.authorize();
        } catch (VerifyGroupException e) {
            LOGGER.warn("Authorization check failed. {}", e.getMessage());
            return new ResponseEntity(new Forbidden(), HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(new Accepted(), HttpStatus.ACCEPTED);
    }

}
