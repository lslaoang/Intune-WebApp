package com.testco.intunewebapp.service.version;

import com.testco.iw.models.AppInformation;

public interface CompatibilityService {
    void verifyVersion(AppInformation appInformation);
}
