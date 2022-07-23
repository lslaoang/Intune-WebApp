package com.testco.intunewebapp.service.version;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Service
public class VersionBodyServiceImpl implements VersionBodyService {

    private static final Logger LOGGER = Logger.getLogger(VersionBodyService.class.getName());

    @Value("${app.version.ios}")
    private List<String> APP_VERSIONS_IOS;

    @Value("${app.version.android}")
    private List<String> APP_VERSIONS_ANDROID;

    @Override
    public void verifyVersion(String appOs, String appVersion) {

        try {
            LOGGER.info("Checking application compatibility started.");

            LOGGER.info("Validating version format ...");
            validateVersionFormat(appVersion);

            LOGGER.info("Checking application version ...");
            checkCompatibility(appOs, appVersion);

        } catch (RuntimeException e) {
            LOGGER.severe("Error occurred while checking version." + e);
            throw new VersionException("Something went wrong.");
        }
        LOGGER.info("Version compatible.");
    }

    @Deprecated
    private String getMajorVersion(String version) {
        return version.length() > 2 ? version.substring(0, version.indexOf(".")) : version;
    }

    private void validateVersionFormat(String version) {
        final String VALID_VERSION_REGEX = "^[1-9]\\d*(\\.[0-9]\\d*){0,3}$";
        if (!version.matches(VALID_VERSION_REGEX)) {
            throw new VersionException("Version format is not valid.");
        }
    }

    private void checkCompatibility(String appOs, String appVersion) {

        Map<String, List<String>> versionMap = new HashMap<>();
        versionMap.put("ios", APP_VERSIONS_IOS);
        versionMap.put("android", APP_VERSIONS_ANDROID);

        if (!versionMap.get(appOs).contains(appVersion)) {
            LOGGER.severe("Version not compatible");
            throw new VersionException("Version not accepted.");
        }
    }

}
