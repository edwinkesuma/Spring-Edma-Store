package com.edwinkesuma.springedmastore.features.common.storage.application.service;

import com.edwinkesuma.springedmastore.features.common.storage.application.dto.ResponseUploadFileDTO;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
    ResponseUploadFileDTO uploadFile(MultipartFile file, String folder);

    void deleteFile(String publicId);
}
