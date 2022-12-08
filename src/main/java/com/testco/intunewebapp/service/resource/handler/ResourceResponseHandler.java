package com.testco.intunewebapp.service.resource.handler;

import com.testco.intunewebapp.service.upload.UploadErrorException;
import org.apache.http.HttpResponse;
import org.apache.http.client.ResponseHandler;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Logger;

@Component
public class ResourceResponseHandler {

    private static final Logger LOGGER = Logger.getLogger(ResourceResponseHandler.class.getName());

    public static ResponseHandler<String> handleResponse = httpResponse -> {
        int status = httpResponse.getStatusLine().getStatusCode();
        LOGGER.info("Status is: "+ status);
        StringBuilder result = resultBody(httpResponse);
        if(!(status >= 200 && status < 300)){
            LOGGER.severe("Error occurred.");
            if(status == 500){
                throw new InternalError();
            }
            throw new UploadErrorException(String.format("Request failed with error code: %d Reason: %s",status,result));
        }
        return String.valueOf(status);
    };

    private static StringBuilder resultBody(HttpResponse httpResponse) throws IOException {
        StringBuilder result = new StringBuilder();
        try (BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(httpResponse.getEntity().getContent()))) {
            String responseLine;
            while((responseLine = bufferedReader.readLine()) != null) {
                result.append(responseLine);
            }
        }
        return result;
    }

    private ResourceResponseHandler(){
    }
}
