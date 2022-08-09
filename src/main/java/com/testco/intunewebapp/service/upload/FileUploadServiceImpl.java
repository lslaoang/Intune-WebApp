package com.testco.intunewebapp.service.upload;

import com.google.gson.Gson;
import com.testco.intunewebapp.model.UploadRequest;
import com.testco.intunewebapp.model.UploadRequestRaw;
import com.testco.intunewebapp.service.prepare.PrepareRequestErrorException;
import com.testco.intunewebapp.service.prepare.PrepareRequestServiceImpl;
import com.testco.iw.models.FileUpload;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.logging.Logger;

import static org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction.clientRegistrationId;

@Service
public class FileUploadServiceImpl implements FileUploadService {

    private final Logger LOGGER = Logger.getLogger(FileUploadService.class.getName());

    private final WebClient webClient;
    private final PrepareRequestServiceImpl prepareRequestService;

    @Value("${provider.apigee.base-uri}")
    String resourceBaseUri;

    @Value("${provider.apigee.file-upload}")
    String resourceEndpoint;



    public FileUploadServiceImpl(WebClient webClient, PrepareRequestServiceImpl prepareRequestService) {
        this.webClient = webClient;
        this.prepareRequestService = prepareRequestService;
    }

    @Override
    public void uploadToResource(FileUpload fileUpload) {

        UploadRequestRaw uploadRequestRaw;
        try{
            uploadRequestRaw = prepareRequestService.convertUploadRequest(fileUpload);
        }catch (RuntimeException e){
            LOGGER.severe("Error occurred while converting the request");
            throw new PrepareRequestErrorException("Error happened while converting the request.");
        }


        try {
            int numberOfCopies = uploadRequestRaw.getMetadata().length;
            System.out.println("Raw Request: " + new Gson().toJson(uploadRequestRaw.getMetadata()));

            int counter = 1;
            while(counter <= numberOfCopies){
                LOGGER.info("Sending file(s) to resource " + counter  + "/" + numberOfCopies);

                UploadRequest uploadRequest = UploadRequest.builder()
                        .uploadFile(uploadRequestRaw.getFile())
                        .uploadMetadata(uploadRequestRaw.getMetadata()[counter - 1])
                        .build();

                sendFile(uploadRequest);
                System.out.println("This is it" + new Gson().toJson(uploadRequest));

                counter++;
            }

        }catch (RuntimeException e){
            LOGGER.severe("Error occurred while uploading file to resource.");
            throw new UploadErrorException("Uploading file(s) to resource failed." + e);
        }
        LOGGER.info("Uploaded successful!");
    }

    private void sendFile(UploadRequest uploadRequest){
        LOGGER.info("This is the full request: " + uploadRequest);
        webClient
                .post()
                .uri(resourceBaseUri + resourceEndpoint)
                .attributes(clientRegistrationId("testco-webapp"))
                .bodyValue(uploadRequest)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
