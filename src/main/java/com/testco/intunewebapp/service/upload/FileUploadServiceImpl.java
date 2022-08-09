package com.testco.intunewebapp.service.upload;

import com.testco.intunewebapp.model.FileUploadRequest;
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

        FileUploadRequest fileUploadRequest;
        try{
            fileUploadRequest = prepareRequestService.convertUploadRequest(fileUpload);
        }catch (RuntimeException e){
            LOGGER.severe("Error occurred while converting the request");
            throw new PrepareRequestErrorException("Error happened while converting the request.");
        }

        try {
            int numberOfCopies = fileUploadRequest.getMetadata().length;

            int counter = 1;
            while(counter <= numberOfCopies){
                LOGGER.info("Sending file(s) to resource " + counter  + "/" + numberOfCopies);
                sendFile(fileUploadRequest);
                counter++;
            }

        }catch (RuntimeException e){
            LOGGER.severe("Error occurred while uploading file to resource.");
            throw new UploadErrorException("Uploading file(s) to resource failed." + e);
        }
        LOGGER.info("Uploaded successful!");
    }

    private void sendFile(FileUploadRequest fileUploadRequest){
        webClient
                .post()
                .uri(resourceBaseUri + resourceEndpoint)
                .attributes(clientRegistrationId("testco-webapp"))
                .bodyValue(fileUploadRequest)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
