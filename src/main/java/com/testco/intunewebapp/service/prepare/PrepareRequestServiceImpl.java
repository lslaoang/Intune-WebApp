package com.testco.intunewebapp.service.prepare;

import com.azure.spring.aad.AADOAuth2AuthenticatedPrincipal;
import com.testco.intunewebapp.model.UploadFile;
import com.testco.intunewebapp.model.UploadMetadata;
import com.testco.intunewebapp.model.UploadRequestRaw;
import com.testco.iw.models.FileUpload;
import com.testco.iw.models.Metadata;
import com.testco.iw.models.MetadataCopies;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

@Service
public class PrepareRequestServiceImpl implements PrepareRequestService {

    Logger LOGGER = Logger.getLogger(PrepareRequestService.class.getName());

    @Override
    public UploadRequestRaw convertUploadRequest(FileUpload fileUpload) {

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

        List<UploadMetadata> uploadMetadata = new ArrayList<>();
        if (isMultipleCopy) {
            List<MetadataCopies> listOfCopies = fileUpload.getMetadata().getCopies();
            for (MetadataCopies listOfCopy : listOfCopies) {
                uploadMetadata.add(addCopyMetadata(metadata, listOfCopy));
            }

        } else {
            uploadMetadata = Collections.singletonList(metadata);
        }

        return UploadRequestRaw.builder()
                .file(uploadFile)
                .metadata(uploadMetadata)
                .build();
    }

    private UploadMetadata addCopyMetadata(UploadMetadata uploadMetadata, MetadataCopies metadataCopies) {

        return UploadMetadata.builder()
                .directProcessing(metadataCopies.isDirectProcessing())
                .scanCountry(uploadMetadata.getScanCountry())
                .fileDestination(uploadMetadata.getFileDestination())
                .copyOfOriginal(uploadMetadata.isCopyOfOriginal())
                .documentDomain(UploadMetadata.DocumentDomain.builder()
                        .businessDomain(uploadMetadata.getDocumentDomain().getBusinessDomain())
                        .businessDocument(uploadMetadata.getDocumentDomain().getBusinessDocument())
                        .businessRelationId(metadataCopies.getRelationNumber())
                        .build())
                .build();
    }

    private UploadMetadata setDefaultMetadata(Metadata metadata) {
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
                .fileInBase64(fileInBase64)
                .build();
    }

    public String generateFileName(){
        final String DOCUMENT_ORIGIN = "scanDocument_";
        final String FILE_TYPE = ".pdf";
        String date = new SimpleDateFormat("yyyy-MM-dd-hhmm").format(new Date());
        String timeStamp = String.valueOf(System.currentTimeMillis());
        return DOCUMENT_ORIGIN + date + "-" + timeStamp + FILE_TYPE;
    }
    
    private String getDestinationId(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        AADOAuth2AuthenticatedPrincipal user = (AADOAuth2AuthenticatedPrincipal) principal;
        return user.getAttribute("preferred_username");
    }
}
