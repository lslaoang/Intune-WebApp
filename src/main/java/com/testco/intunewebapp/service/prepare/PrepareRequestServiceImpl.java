package com.testco.intunewebapp.service.prepare;

import com.azure.spring.aad.AADOAuth2AuthenticatedPrincipal;
import com.testco.intunewebapp.model.FileUploadRequest;
import com.testco.intunewebapp.model.UploadFile;
import com.testco.intunewebapp.model.UploadMetadata;
import com.testco.iw.models.FileUpload;
import com.testco.iw.models.Metadata;
import com.testco.iw.models.MetadataCopies;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

@Service
public class PrepareRequestServiceImpl implements PrepareRequestService {

    Logger LOGGER = Logger.getLogger(PrepareRequestService.class.getName());

    @Override
    public FileUploadRequest convertUploadRequest(FileUpload fileUpload) {

        UploadMetadata metadata =  setDefaultMetadata(fileUpload.getMetadata());
        UploadFile uploadFile = setUploadFile(fileUpload.getFile());

        int copySize;
        boolean isMultipleCopy = false;
        try{
            copySize = fileUpload.getMetadata().getCopies().size();
            if(copySize > 0){
                isMultipleCopy = true;
                LOGGER.info("Multiple copy requests detected.");
            }
        }catch (RuntimeException e){
            LOGGER.info("Multiple copy not detected in the request.");
        }

        if(isMultipleCopy){
            List<MetadataCopies> listOfCopies = fileUpload.getMetadata().getCopies();
            for(MetadataCopies metadataCopies : listOfCopies){
                addCopyMetadata(metadata, metadataCopies);
            }
        }

        return FileUploadRequest.builder()
                .file(uploadFile)
                .metadata(metadata)
                .build();
    }

    private UploadMetadata addCopyMetadata(UploadMetadata uploadMetadata, MetadataCopies metadataCopies){
       uploadMetadata.setDirectProcessing(metadataCopies.isDirectProcessing());
       uploadMetadata.getDocumentDomain().setBusinessRelationId(metadataCopies.getRelationNumber());
       return uploadMetadata;
    }

    private UploadMetadata setDefaultMetadata(Metadata metadata){
        UploadMetadata.DocumentDomain documentDomain = UploadMetadata.DocumentDomain.builder()
                .businessDocument(metadata.getBusinessDomain())
                .businessDomain(metadata.getBusinessDomain())
                .build();
        UploadMetadata.FileDestination fileDestination = UploadMetadata.FileDestination.builder()
                .fileDestinationInbox(Collections.singletonList(getDestinationId()))
                .build();

       return  UploadMetadata.builder()
                .copyOfOriginal(metadata.isCopyOfOriginal())
                .documentDomain(documentDomain)
                .fileDestination(fileDestination)
                .scanCountry(metadata.getCountry())
                .build();
    }

    private UploadFile setUploadFile(String fileInBase64){
        return UploadFile.builder()
                .fileName("SampleFileName.pdf")
                .fileInBase64(fileInBase64)
                .build();
    }
    
    private String getDestinationId(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        AADOAuth2AuthenticatedPrincipal user = (AADOAuth2AuthenticatedPrincipal) principal;
        return user.getAttribute("preferred_username");
    }
}
