package com.edwinkesuma.springedmastore.features.product.application.usecase.impl.category;

import com.edwinkesuma.springedmastore.common.exception.DuplicateResourceException;
import com.edwinkesuma.springedmastore.common.exception.ResourceNotFoundException;
import com.edwinkesuma.springedmastore.features.common.storage.application.dto.ResponseUploadFileDTO;
import com.edwinkesuma.springedmastore.features.common.storage.infrastructure.cloudinary.CloudinaryStorageService;
import com.edwinkesuma.springedmastore.features.product.application.dto.RequestUpdateCategoryDTO;
import com.edwinkesuma.springedmastore.features.product.application.dto.ResponseCategoryDTO;
import com.edwinkesuma.springedmastore.features.product.application.mapper.CategoryMapper;
import com.edwinkesuma.springedmastore.features.product.domain.entity.Category;
import com.edwinkesuma.springedmastore.features.product.domain.repository.CategoryRepository;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateCategoryUseCaseImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @Mock
    private CloudinaryStorageService cloudinaryStorageService;

    @InjectMocks
    private UpdateCategoryUseCaseImpl updateCategoryUseCase;

    @Test
    void execute_shouldUpdateCategoryWithoutImageSuccessfully() throws BadRequestException {

        UUID categoryId = UUID.randomUUID();

        RequestUpdateCategoryDTO request =
                new RequestUpdateCategoryDTO(
                        "Updated Food",
                        null,
                        1,
                        true
                );

        Category category = new Category();
        category.setName("Food");
        category.setSlug("food");

        Category savedCategory = new Category();
        savedCategory.setName("Updated Food");
        savedCategory.setSlug("updated-food");

        ResponseCategoryDTO responseDTO =
                mock(ResponseCategoryDTO.class);

        when(categoryRepository.findById(categoryId))
                .thenReturn(Optional.of(category));

        when(categoryRepository.existsBySlugAndIdNot(
                "food",
                categoryId
        )).thenReturn(false);

        when(categoryRepository.save(category))
                .thenReturn(savedCategory);

        when(categoryMapper.toCategoryDTO(savedCategory))
                .thenReturn(responseDTO);

        ResponseCategoryDTO result =
                updateCategoryUseCase.execute(
                        request,
                        categoryId,
                        null
                );

        assertNotNull(result);
        assertEquals(responseDTO, result);

        assertEquals("Updated Food", category.getName());
        assertEquals("updated-food", category.getSlug());

        verify(categoryRepository)
                .findById(categoryId);

        verify(categoryRepository)
                .save(category);

        verifyNoInteractions(cloudinaryStorageService);
    }

    @Test
    void execute_shouldUpdateCategoryWithImageSuccessfully() throws BadRequestException {

        UUID categoryId = UUID.randomUUID();

        RequestUpdateCategoryDTO request =
                new RequestUpdateCategoryDTO(
                        "Updated Food",
                        null,
                        1,
                        true
                );

        MultipartFile image = new MockMultipartFile(
                "image",
                "food.jpg",
                "image/jpeg",
                "dummy-image".getBytes()
        );

        Category category = new Category();
        category.setName("Food");
        category.setSlug("food");
        category.setIconPublicId("old-image");

        ResponseUploadFileDTO uploadedImage =
                new ResponseUploadFileDTO(
                        "https://cloudinary.com/new-image.jpg",
                        "categories/new-image"
                );

        Category savedCategory = new Category();

        ResponseCategoryDTO responseDTO =
                mock(ResponseCategoryDTO.class);

        when(categoryRepository.findById(categoryId))
                .thenReturn(Optional.of(category));

        when(categoryRepository.existsBySlugAndIdNot(
                "food",
                categoryId
        )).thenReturn(false);

        when(cloudinaryStorageService.uploadFile(
                image,
                "categories"
        )).thenReturn(uploadedImage);

        when(categoryRepository.save(category))
                .thenReturn(savedCategory);

        when(categoryMapper.toCategoryDTO(savedCategory))
                .thenReturn(responseDTO);

        ResponseCategoryDTO result =
                updateCategoryUseCase.execute(
                        request,
                        categoryId,
                        image
                );

        assertNotNull(result);

        assertEquals(
                "https://cloudinary.com/new-image.jpg",
                category.getIconUrl()
        );

        assertEquals(
                "categories/new-image",
                category.getIconPublicId()
        );

        verify(cloudinaryStorageService)
                .deleteFile("old-image");

        verify(cloudinaryStorageService)
                .uploadFile(image, "categories");

        verify(categoryRepository)
                .save(category);
    }

    @Test
    void execute_shouldThrowResourceNotFoundException_whenCategoryNotFound() {

        UUID categoryId = UUID.randomUUID();

        RequestUpdateCategoryDTO request =
                new RequestUpdateCategoryDTO(
                        "Updated Food",
                        null,
                        1,
                        true
                );

        when(categoryRepository.findById(categoryId))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> updateCategoryUseCase.execute(
                        request,
                        categoryId,
                        null
                )
        );

        verify(categoryRepository)
                .findById(categoryId);

        verifyNoInteractions(
                categoryMapper,
                cloudinaryStorageService
        );
    }

    @Test
    void execute_shouldThrowDuplicateResourceException_whenSlugAlreadyExists() {

        UUID categoryId = UUID.randomUUID();

        RequestUpdateCategoryDTO request =
                new RequestUpdateCategoryDTO(
                        "Updated Food",
                        null,
                        1,
                        true
                );

        Category category = new Category();
        category.setSlug("food");

        when(categoryRepository.findById(categoryId))
                .thenReturn(Optional.of(category));

        when(categoryRepository.existsBySlugAndIdNot(
                "food",
                categoryId
        )).thenReturn(true);

        assertThrows(
                DuplicateResourceException.class,
                () -> updateCategoryUseCase.execute(
                        request,
                        categoryId,
                        null
                )
        );

        verify(categoryRepository)
                .findById(categoryId);

        verify(categoryRepository)
                .existsBySlugAndIdNot("food", categoryId);

        verifyNoInteractions(
                categoryMapper,
                cloudinaryStorageService
        );
    }

    @Test
    void execute_shouldThrowBadRequestException_whenImageContentTypeInvalid() {

        UUID categoryId = UUID.randomUUID();

        RequestUpdateCategoryDTO request =
                new RequestUpdateCategoryDTO(
                        "Updated Food",
                        null,
                        1,
                        true
                );

        MultipartFile image = new MockMultipartFile(
                "image",
                "file.pdf",
                "application/pdf",
                "dummy".getBytes()
        );

        Category category = new Category();
        category.setSlug("food");

        when(categoryRepository.findById(categoryId))
                .thenReturn(Optional.of(category));

        when(categoryRepository.existsBySlugAndIdNot(
                "food",
                categoryId
        )).thenReturn(false);

        assertThrows(
                BadRequestException.class,
                () -> updateCategoryUseCase.execute(
                        request,
                        categoryId,
                        image
                )
        );

        verifyNoInteractions(cloudinaryStorageService);
    }

    @Test
    void execute_shouldNotDeleteOldImage_whenIconPublicIdIsNull() throws BadRequestException {

        UUID categoryId = UUID.randomUUID();

        RequestUpdateCategoryDTO request =
                new RequestUpdateCategoryDTO(
                        "Updated Food",
                        null,
                        1,
                        true
                );

        MultipartFile image = new MockMultipartFile(
                "image",
                "food.jpg",
                "image/jpeg",
                "dummy-image".getBytes()
        );

        Category category = new Category();
        category.setSlug("food");
        category.setIconPublicId(null);

        ResponseUploadFileDTO uploadedImage =
                new ResponseUploadFileDTO(
                        "https://cloudinary.com/image.jpg",
                        "categories/image123"
                );

        when(categoryRepository.findById(categoryId))
                .thenReturn(Optional.of(category));

        when(categoryRepository.existsBySlugAndIdNot(
                "food",
                categoryId
        )).thenReturn(false);

        when(cloudinaryStorageService.uploadFile(
                image,
                "categories"
        )).thenReturn(uploadedImage);

        when(categoryRepository.save(category))
                .thenReturn(category);

        when(categoryMapper.toCategoryDTO(category))
                .thenReturn(mock(ResponseCategoryDTO.class));

        updateCategoryUseCase.execute(
                request,
                categoryId,
                image
        );

        verify(cloudinaryStorageService, never())
                .deleteFile(anyString());

        verify(cloudinaryStorageService)
                .uploadFile(image, "categories");
    }
}