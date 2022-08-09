package com.testco.intunewebapp.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class FileUploadRequest {
    private UploadFile file;
    private UploadMetadata[] metadata;
}
