package com.testco.intunewebapp.util;

import com.azure.spring.aad.AADOAuth2AuthenticatedPrincipal;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;

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

    @Value("${spring.profiles.active}")
    static String activeProfile;

    public static String printObjectAsString(Object anyObject) {
//        if (!activeProfile.equals("prod")) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
//            System.out.println("Object content: =========> \n" + gson.toJson(anyObject));
            return gson.toJson(anyObject);
//        }
    }

    public static String getUserNameFromRequest(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        AADOAuth2AuthenticatedPrincipal user = (AADOAuth2AuthenticatedPrincipal) principal;
        String userEmail = user.getAttribute("preferred_username");
        LOGGER.info("Username successfully fetched!");
        return userEmail;
    }
}
