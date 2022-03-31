package com.testco.intunewebapp.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UnAuthorizedAccess {
    @JsonProperty
    final int status = 401;
    @JsonProperty
    final String title = "Failed to authenticate request.";
}