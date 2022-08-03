package com.testco.intunewebapp.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class UploadMetadata {
    private String scanCountry;
    private boolean copyOfOriginal;
    private DocumentDomain documentDomain;
    private FileDestination fileDestination;
    private boolean directProcessing;

    @Getter
    @Setter
    @Builder
    private static class DocumentDomain {
        private String businessDomain;
        private String businessDocument;
        private String businessRelationId;
    }

    @Getter
    @Setter
    @Builder
    private static class FileDestination {
        private List<String> fileDestinationInbox;
    }
}
