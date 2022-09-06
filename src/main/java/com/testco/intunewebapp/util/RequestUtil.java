package com.testco.intunewebapp.util;

import java.util.logging.Logger;

public class RequestUtil {
        final static Logger LOGGER = Logger.getLogger(RequestUtil.class.getName());

    public static void validateVersionFormat(String version) {
        final String VALID_VERSION_REGEX = "^[1-9]\\d*(\\.[0-9]\\d*){0,2}$";

        if (!version.matches(VALID_VERSION_REGEX)) {
            LOGGER.severe("Invalid version format detected. Submitted version:  " + version);
            throw new RuntimeException("Version format is not valid.");
        }
        LOGGER.info("Valid version format.");
    }
}
