package com.testco.intunewebapp.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UploadRequest {
    private UploadFile uploadFile;
    private UploadMetadata uploadMetadata;
}
