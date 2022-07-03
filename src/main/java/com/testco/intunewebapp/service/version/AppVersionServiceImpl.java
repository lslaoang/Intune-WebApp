package com.testco.intunewebapp.service.version;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.logging.Logger;

@Service
public class AppVersionServiceImpl implements AppVersionService {

    private static final Logger LOGGER = Logger.getLogger(AppVersionService.class.getName());

    protected final String APP_VERSION_HEADER = "X-UBS-APP-VERSION";
    protected final String APP_OS_HEADER = "X-UBS-APP-OS";

    @Value("{app.version.ios}")
    private String APP_VERSION_IOS;

    @Value("{app.version.android}")
    private String APP_VERSION_ANDROID;

    @Override
    public boolean isCorrectVersion(HttpServletRequest request) {
        String appVersion, appOS;
        try {
            appOS = request.getHeader(APP_OS_HEADER);
            appVersion = request.getHeader(APP_VERSION_HEADER);
            if(appOS.equalsIgnoreCase("ios")){
                if (!appVersion.equals(APP_VERSION_IOS)) {
                    return false;
                }
            }
            if(appOS.equalsIgnoreCase("android")){
                if (!appVersion.equals(APP_VERSION_ANDROID)) {
                    return false;
                }
            }
        } catch (NullPointerException e){
            LOGGER.severe("App version not detected.");
            return false;
        }
        LOGGER.info("Valid application version.");
        return true;
    }
}
