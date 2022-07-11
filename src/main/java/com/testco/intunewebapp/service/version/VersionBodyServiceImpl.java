package com.testco.intunewebapp.service.version;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@Service
public class VersionBodyServiceImpl implements VersionBodyService{

    private static final Logger LOGGER = Logger.getLogger(VersionBodyService.class.getName());

    @Value("${app.version.ios}")
    private String APP_VERSION_IOS;

    @Value("${app.version.android}")
    private String APP_VERSION_ANDROID;

    @Override
    public void verifyVersion(String appOs, String appVersion) {

        try{
            LOGGER.info("Checking application version...");
            Map<String, String> appVersionMap = new HashMap<>();
            appVersionMap.put("ios", APP_VERSION_IOS);
            appVersionMap.put("android", APP_VERSION_ANDROID);

            if(!appVersion.equals(appVersionMap.get(appOs))){
                LOGGER.severe("Version not compatible");
                throw new VersionException("Version not accepted.");
            }

        }catch (RuntimeException e){
            LOGGER.severe("Error occurred while checking version.");
            throw new VersionException("Something went wrong.");
        }

    }
}
