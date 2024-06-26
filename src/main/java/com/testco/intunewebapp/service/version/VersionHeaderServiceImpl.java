package com.testco.intunewebapp.service.version;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Logger;

@Service
public class VersionHeaderServiceImpl implements VersionHeaderService {

    private static final Logger LOGGER = Logger.getLogger(VersionHeaderService.class.getName());

    protected final String APP_VERSION_HEADER = "X-UBS-APP-VERSION";
    protected final String APP_OS_HEADER = "X-UBS-APP-OS";

    @Value("${app.version.ios}")
    private List<String> APP_VERSION_IOS;

    @Value("${app.version.android}")
    private List<String> APP_VERSION_ANDROID;

    @Override
    public void verifyVersion(HttpServletRequest request) {

        LOGGER.info("Checking application version...");
        Map<String, List<String>> appVersionMap = new HashMap<>();
        appVersionMap.put("ios", APP_VERSION_IOS);
        appVersionMap.put("android", APP_VERSION_ANDROID);

        String requestVersion, requestOs;
        try {
            requestOs = request.getHeader(APP_OS_HEADER).toLowerCase(Locale.ROOT);
            requestVersion = request.getHeader(APP_VERSION_HEADER).toLowerCase(Locale.ROOT);

            List<String> appVersion = appVersionMap.get(requestOs);
            if (!appVersion.contains(requestVersion)) {
                LOGGER.warning(String.format("Invalid application version. Application should be %s version.", appVersion));
                throw new VersionException("Invalid version.");
            }
        } catch (NullPointerException e) {
            LOGGER.severe("Application version or operating system not detected." );
            throw new VersionException("Required header not found.");
        }
        LOGGER.info("Valid application version.");
    }
}
