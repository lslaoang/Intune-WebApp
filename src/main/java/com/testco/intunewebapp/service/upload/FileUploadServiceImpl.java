package com.testco.intunewebapp.service.upload;

import com.testco.iw.models.FileUpload;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class FileUploadServiceImpl implements FileUploadService {

    private final Logger LOGGER = Logger.getLogger(FileUploadService.class.getName());

    @Override
    public void uploadToResource(FileUpload fileUpload) {


        LOGGER.info("Uploaded successful!");
    }
}
