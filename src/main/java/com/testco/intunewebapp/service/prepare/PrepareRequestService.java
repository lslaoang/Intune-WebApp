package com.testco.intunewebapp.service.prepare;

import com.testco.intunewebapp.model.UploadRequestRaw;
import com.testco.iw.models.FileUpload;

public interface PrepareRequestService {
    UploadRequestRaw convertUploadRequest(FileUpload fileUpload);
}
