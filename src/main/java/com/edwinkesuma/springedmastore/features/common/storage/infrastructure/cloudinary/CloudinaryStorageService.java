package com.edwinkesuma.springedmastore.features.common.storage.infrastructure.cloudinary;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.edwinkesuma.springedmastore.common.exception.FileDeletionException;
import com.edwinkesuma.springedmastore.common.exception.FileUploadException;
import com.edwinkesuma.springedmastore.features.common.storage.application.dto.ResponseUploadFileDTO;
import com.edwinkesuma.springedmastore.features.common.storage.application.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudinaryStorageService
        implements FileStorageService {

    private final Cloudinary cloudinary;

    @Override
    public ResponseUploadFileDTO uploadFile(
            MultipartFile file,
            String folder
    ) {
        try {

            Map<String, Object> options = ObjectUtils.asMap(
                    "folder", folder
            );

            Map<?, ?> result = cloudinary.uploader()
                    .upload(file.getBytes(), options);

            Object secureUrlObj = result.get("secure_url");
            Object publicIdObj = result.get("public_id");

            if (secureUrlObj == null || publicIdObj == null) {
                throw new FileUploadException("Failed to upload image to Cloudinary");
            }

            return new ResponseUploadFileDTO(
                    secureUrlObj.toString(),
                    publicIdObj.toString()
            );

        } catch (IOException e) {
            throw new FileUploadException("Failed to upload file to Cloudinary");
        }
    }

    @Override
    public void deleteFile(String publicId) {

        try {

            Map<?, ?> result = cloudinary.uploader()
                    .destroy(publicId, ObjectUtils.emptyMap());

            String status = result.get("result").toString();

            if (!status.equals("ok")
                    && !status.equals("not found")) {

                throw new FileDeletionException(
                        "Failed deleting file from Cloudinary"
                );
            }

        } catch (IOException e) {

            throw new FileDeletionException(
                    "Failed deleting file from Cloudinary: " + e.getMessage()
            );
        }
    }
}