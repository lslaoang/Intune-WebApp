package com.testco.intunewebapp.service.version;

import com.testco.iw.models.AppInformation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static com.testco.intunewebapp.util.RequestUtil.validateVersionFormat;


@Service
public class CompatibilityServiceImpl implements CompatibilityService {
    
    private static final String ANDROID = "android";
    private static final String IOS = "android";
    private static final String MAJOR = "major";
    private static final String MINOR = "minor";

    private static final Logger LOGGER = Logger.getLogger(CompatibilityService.class.getName());

    @Value("${application.version.android}")
    private String ANDROID_VERSION;

    @Value("${application.version.ios}")
    private String IOS_VERSION;

    @Override
    public void verifyVersion(AppInformation appInformation) {
        String os = appInformation.getAppOs();
        String version = appInformation.getAppVersion();
        try {

            validateVersionFormat(version);
            if (os.equalsIgnoreCase(ANDROID)) {
                validateAppVersion(getRevision(ANDROID_VERSION), getRevision(version));
            } else if (os.equalsIgnoreCase(IOS)) {
                validateAppVersion(getRevision(IOS_VERSION), getRevision(version));
            } else {
                LOGGER.severe("Unknown operating system. " + os);
                throw new VersionException();
            }
        } catch (RuntimeException e) {
            LOGGER.severe("Error occurred while checking app version. " + e.getMessage());
            throw new VersionException();
        }
        LOGGER.info(String.format("Version %s is compatible!", version));
    }

    private void validateAppVersion(Map<String, Integer> acceptedVersionMap, Map<String, Integer> versionMap) {
        if (!Objects.equals(versionMap.get(MAJOR), acceptedVersionMap.get(MAJOR))
                && (versionMap.get(MINOR)) >= acceptedVersionMap.get(MINOR)) {
            throw new RuntimeException("Version is NOT compatible");
        }
    }

    private Map<String, Integer> getRevision(String version) {
        List<String> versionList = Arrays.stream(version.split("\\.")).collect(Collectors.toList());
        Map<String, Integer> versionMap = new HashMap<>();
        versionMap.put(MAJOR, Integer.parseInt(versionList.get(0)));
        versionMap.put(MINOR, Integer.parseInt(versionList.get(1)));
        return versionMap;
    }

}
