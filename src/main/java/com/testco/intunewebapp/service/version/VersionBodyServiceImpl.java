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
    public void verifyVersion(String reqAppOs, String reqAppVersion) {

        try{
            LOGGER.info("Checking application version...");
            validateVersion(reqAppVersion);

            String okVersionAndroid = majorVersion(APP_VERSION_ANDROID);
            String okVersionIos = majorVersion(APP_VERSION_IOS);

            Map<String, String> appVersionMap = new HashMap<>();
            appVersionMap.put("ios", okVersionIos);
            appVersionMap.put("android", okVersionAndroid);

            if(!appVersionMap.get(reqAppOs).equals(majorVersion(reqAppVersion))){
                LOGGER.severe("Version not compatible");
                throw new VersionException("Version not accepted.");
            }

        }catch (RuntimeException e){
            LOGGER.severe("Error occurred while checking version." + e);
            throw new VersionException("Something went wrong.");
        }
        LOGGER.info("Version compatible.");
    }

    private String majorVersion(String version){
        return version.length() > 2 ? version.substring(0, version.indexOf(".")) : version;
    }

    private void validateVersion(String version){
        final String VALID_VERSION_REGEX = "^[1-9]\\d*(\\.[0-9]\\d*){0,3}$";
        if(!version.matches(VALID_VERSION_REGEX)){
            throw new VersionException("Version format is not valid.");
        }

    }

}
