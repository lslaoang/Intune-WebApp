package com.testco.intunewebapp.service.version;

import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.logging.Logger;

@Service
public class AppVersionServiceImpl implements AppVersionService {

    private static final Logger LOGGER = Logger.getLogger(AppVersionService.class.getName());

    private final String APP_VERSION_HEADER = "X-UBS-APP-VERSION";
    private final String APP_VERSION_VALID = "1.2.0";

    @Override
    public boolean isCorrectVersion(HttpServletRequest request) {
        String appVersion;
        try {
            appVersion = request.getHeader(APP_VERSION_HEADER);
            if (!appVersion.equals(APP_VERSION_VALID)) {
                return false;
            }
        } catch (NullPointerException e){
            LOGGER.severe("App version not detected.");
            return false;
        }
        LOGGER.info("Valid application version.");
        return true;
    }
}
