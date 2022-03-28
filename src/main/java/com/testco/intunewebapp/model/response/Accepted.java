package com.testco.intunewebapp.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Accepted {
    @JsonProperty
    final int status = 200;
    @JsonProperty
    final String title = "Accepted";
}
