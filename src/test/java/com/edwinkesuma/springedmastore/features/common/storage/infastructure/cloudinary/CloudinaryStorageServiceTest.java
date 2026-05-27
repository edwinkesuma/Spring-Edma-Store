package com.edwinkesuma.springedmastore.features.common.storage.infastructure.cloudinary;

import com.cloudinary.Cloudinary;
import com.cloudinary.Uploader;
import com.cloudinary.utils.ObjectUtils;
import com.edwinkesuma.springedmastore.common.exception.FileDeletionException;
import com.edwinkesuma.springedmastore.common.exception.FileUploadException;
import com.edwinkesuma.springedmastore.features.common.storage.application.dto.ResponseUploadFileDTO;
import com.edwinkesuma.springedmastore.features.common.storage.infrastructure.cloudinary.CloudinaryStorageService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CloudinaryStorageServiceTest {

    @Mock
    private Cloudinary cloudinary;

    @Mock
    private Uploader uploader;

    @InjectMocks
    private CloudinaryStorageService cloudinaryStorageService;

    @BeforeEach
    void setUp() {
        when(cloudinary.uploader()).thenReturn(uploader);
    }

    @Test
    void uploadFile_shouldReturnResponseUploadFileDTO_whenUploadSuccess()
            throws Exception {

        MultipartFile file = new MockMultipartFile(
                "file",
                "image.jpg",
                "image/jpeg",
                "dummy-image".getBytes()
        );

        Map<String, Object> uploadResult = Map.of(
                "secure_url", "https://res.cloudinary.com/demo/image.jpg",
                "public_id", "products/image123"
        );

        when(uploader.upload(any(byte[].class), any(Map.class)))
                .thenReturn(uploadResult);

        ResponseUploadFileDTO result =
                cloudinaryStorageService.uploadFile(file, "products");

        assertNotNull(result);
        assertEquals(
                "https://res.cloudinary.com/demo/image.jpg",
                result.imageUrl()
        );
        assertEquals(
                "products/image123",
                result.publicId()
        );

        verify(uploader).upload(
                any(byte[].class),
                eq(ObjectUtils.asMap("folder", "products"))
        );
    }

    @Test
    void uploadFile_shouldThrowFileUploadException_whenIOExceptionOccurs()
            throws Exception {

        MultipartFile file = new MockMultipartFile(
                "file",
                "image.jpg",
                "image/jpeg",
                "dummy-image".getBytes()
        );

        when(uploader.upload(any(byte[].class), any(Map.class)))
                .thenThrow(new IOException("Upload failed"));

        assertThrows(
                FileUploadException.class,
                () -> cloudinaryStorageService.uploadFile(file, "products")
        );
    }

    @Test
    void deleteFile_shouldSuccess_whenResultIsOk()
            throws Exception {

        Map<String, Object> destroyResult = Map.of(
                "result", "ok"
        );

        when(uploader.destroy(anyString(), any(Map.class)))
                .thenReturn(destroyResult);

        assertDoesNotThrow(() ->
                cloudinaryStorageService.deleteFile("image123")
        );

        verify(uploader).destroy(
                eq("image123"),
                eq(ObjectUtils.emptyMap())
        );
    }

    @Test
    void deleteFile_shouldSuccess_whenResultIsNotFound()
            throws Exception {

        Map<String, Object> destroyResult = Map.of(
                "result", "not found"
        );

        when(uploader.destroy(anyString(), any(Map.class)))
                .thenReturn(destroyResult);

        assertDoesNotThrow(() ->
                cloudinaryStorageService.deleteFile("image123")
        );
    }

    @Test
    void deleteFile_shouldThrowFileDeletionException_whenResultIsInvalid()
            throws Exception {

        Map<String, Object> destroyResult = Map.of(
                "result", "error"
        );

        when(uploader.destroy(anyString(), any(Map.class)))
                .thenReturn(destroyResult);

        assertThrows(
                FileDeletionException.class,
                () -> cloudinaryStorageService.deleteFile("image123")
        );
    }

    @Test
    void deleteFile_shouldThrowFileDeletionException_whenIOExceptionOccurs()
            throws Exception {

        when(uploader.destroy(anyString(), any(Map.class)))
                .thenThrow(new IOException("Delete failed"));

        assertThrows(
                FileDeletionException.class,
                () -> cloudinaryStorageService.deleteFile("image123")
        );
    }
}
