package com.testco.intunewebapp.service.prepare;

import com.testco.intunewebapp.model.FileUploadRequest;
import com.testco.intunewebapp.model.UploadFile;
import com.testco.intunewebapp.model.UploadMetadata;
import com.testco.iw.models.FileUpload;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class PrepareRequestServiceImpl implements PrepareRequestService {

    @Override
    public FileUploadRequest convertUploadRequest(FileUpload fileUpload) {

        UploadFile uploadFile = UploadFile.builder()
                .fileName("SampleFileName")
                .fileInBase64("sampleFileInBase64")
                .build();

        UploadMetadata.DocumentDomain documentDomain = UploadMetadata.DocumentDomain.builder()
                .businessDocument("SampleBusinessDocument")
                .businessDomain("WM")
                .businessRelationId("0019")
                .build();
        UploadMetadata.FileDestination fileDestination = UploadMetadata.FileDestination.builder()
                .fileDestinationInbox(Collections.singletonList("sample.inbox@test.com"))
                .build();

        UploadMetadata uploadMetadata = UploadMetadata.builder()
                .copyOfOriginal(true)
                .directProcessing(false)
                .documentDomain(documentDomain)
                .fileDestination(fileDestination)
                .scanCountry("PH")
                .build();

        return FileUploadRequest.builder()
                .file(uploadFile)
                .metadata(uploadMetadata)
                .build();
    }
}
