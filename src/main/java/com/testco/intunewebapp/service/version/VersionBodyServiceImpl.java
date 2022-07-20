package com.testco.intunewebapp.service.version;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
            LOGGER.info("Checking application compatibility started.");

            LOGGER.info("Validating version format ...");
            validateVersionFormat(appVersion);

            LOGGER.info("Checking application version ...");
            checkCompatibility(appOs, appVersion);



        }catch (RuntimeException e){
            LOGGER.severe("Error occurred while checking version." + e);
            throw new VersionException("Something went wrong.");
        }
        LOGGER.info("Version compatible.");
    }

    private String getMajorVersion(String version){
        return version.length() > 2 ? version.substring(0, version.indexOf(".")) : version;
    }

    private void validateVersionFormat(String version){
        final String VALID_VERSION_REGEX = "^[1-9]\\d*(\\.[0-9]\\d*){0,3}$";
        if(!version.matches(VALID_VERSION_REGEX)){
            throw new VersionException("Version format is not valid.");
        }

    }

    private void checkCompatibility(String appOs, String appVersion){
        String androidVersion = getMajorVersion(APP_VERSION_ANDROID);
        String iosVersion = getMajorVersion(APP_VERSION_IOS);

        Map<String, List<String>> versionMap = new HashMap<>();
        List<String> iosVersionList = new ArrayList<>();
        iosVersionList.add(iosVersion);
        iosVersionList.add("1.1.999999999999");
        iosVersionList.add("1.2.3");

        List<String> androidVersionList = new ArrayList<>();
        androidVersionList.add(androidVersion);
        androidVersionList.add("1.1.999999999999");
        androidVersionList.add("1.2.3");


        versionMap.put("ios", iosVersionList);
        versionMap.put("android", androidVersionList);

        if(!versionMap.get(appOs).contains(appVersion)){
            LOGGER.severe("Version not compatible");
            throw new VersionException("Version not accepted.");
        }
    }

}
