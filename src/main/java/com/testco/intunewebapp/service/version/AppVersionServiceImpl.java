package com.testco.intunewebapp.service.version;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@Service
public class AppVersionServiceImpl implements AppVersionService {

    private static final Logger LOGGER = Logger.getLogger(AppVersionService.class.getName());

    protected final String APP_VERSION_HEADER = "X-UBS-APP-VERSION";
    protected final String APP_OS_HEADER = "X-UBS-APP-OS";

    @Value("${app.version.ios}")
    private String APP_VERSION_IOS;

    @Value("${app.version.android}")
    private String APP_VERSION_ANDROID;

    @Override
    public boolean isCorrectVersion(HttpServletRequest request) {

        LOGGER.info("Checking application version...");
        Map<String, String> appVersionMap = new HashMap<>();
        appVersionMap.put("ios", APP_VERSION_IOS);
        appVersionMap.put("android", APP_VERSION_ANDROID);

        String requestVersion, requestOs;
        try {
            requestOs = request.getHeader(APP_OS_HEADER);
            requestVersion = request.getHeader(APP_VERSION_HEADER);

            String appVersion = appVersionMap.get(requestOs.toLowerCase());
            if (!appVersion.equals(requestVersion)) {
                LOGGER.warning(String.format("Invalid application version. Application should be %s version.", appVersion));
                return false;
            }
        } catch (NullPointerException e) {
            LOGGER.severe("Application version or operating system not detected." + e.getMessage());
            return false;
        }
        LOGGER.info("Valid application version.");
        return true;
    }
}
