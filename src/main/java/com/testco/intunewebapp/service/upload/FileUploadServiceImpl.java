package com.testco.intunewebapp.service.upload;

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

    @Value("${provider.apigee.base-uri}")
    String resourceBaseUri;

    @Value("${provider.apigee.verify}")
    String resourceEndpoint;



    public FileUploadServiceImpl(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public void uploadToResource(FileUpload fileUpload) {

        try {
            int numberOfCopies = fileUpload.getMetadata().getNumberOfCopies();

            int counter = 0;
            while(counter < numberOfCopies){
                LOGGER.info("Sending file(s) to resource " + counter + "/" + numberOfCopies);
                sendFile(fileUpload);
                counter++;
            }

        }catch (RuntimeException e){
            LOGGER.severe("Error occurred while uploading file to resource.");
            throw new UploadErrorException("Uploading file(s) to resource failed." + e);
        }
        LOGGER.info("Uploaded successful!");
    }

    private void sendFile(FileUpload fileUpload){
        webClient
                .post()
                .uri(resourceBaseUri + resourceEndpoint)
                .attributes(clientRegistrationId("testco-webapp"))
                .bodyValue(fileUpload)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
