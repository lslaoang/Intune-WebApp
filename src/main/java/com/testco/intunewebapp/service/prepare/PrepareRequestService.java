package com.testco.intunewebapp.service.prepare;

import com.testco.intunewebapp.model.FileUploadRequest;
import com.testco.iw.models.FileUpload;

public interface PrepareRequestService {
    FileUploadRequest convertUploadRequest(FileUpload fileUpload);
}
