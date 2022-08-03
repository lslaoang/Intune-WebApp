package com.testco.intunewebapp.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UploadMetadata {
    private String scanCountry;
    private boolean copyOfOriginal;
    private String businessDomain;
    private String businessDocument;
    private String businessRelationId;
    private boolean directProcessing;
}
