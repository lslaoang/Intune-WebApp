package com.testco.intunewebapp.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class UploadRequestRaw {
    private UploadFile file;
    private List<UploadMetadata> metadata;
}
