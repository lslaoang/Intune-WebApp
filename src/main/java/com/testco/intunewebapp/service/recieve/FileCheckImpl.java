package com.testco.intunewebapp.service.recieve;

import com.testco.iw.models.FileUpload;
import com.testco.iw.models.Metadata;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class FileCheckImpl implements  FileCheck{

    private static Logger LOGGER = Logger.getLogger(FileCheckImpl.class.getName());

    public boolean validUpload(FileUpload fileUpload) {
        if (fileUpload == null){
            return false;
        }
        System.out.println(literalShit("shit it is"));

        if (fileUpload.getFile().isEmpty()){
            return false;
        }
        Metadata metadata;
        try{
            metadata =  fileUpload.getMetadata();
            LOGGER.info("Metadata is present: " + metadata);
        }catch (RuntimeException e){
            LOGGER.severe("Metadata validation failed. " + e);
            return false;
        }
        LOGGER.info("Metadata is valid.");
        return true;
    }

    private String literalShit(String shit){
        return "aShit" + shit;
    }

}
