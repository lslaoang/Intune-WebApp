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

        if (fileUpload.getFile().isEmpty()){
            return false;
        }
        Metadata metadata;
        try{
            metadata =  fileUpload.getMetadata();
            LOGGER.info("Metadata is present: " + metadata);
        }catch (RuntimeException e){
            return false;
        }

        return true;
    }

}
