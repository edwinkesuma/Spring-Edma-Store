package com.edwinkesuma.springedmastore.features.product.application.usecase.impl.category;

import com.edwinkesuma.springedmastore.common.exception.DuplicateResourceException;
import com.edwinkesuma.springedmastore.features.common.storage.application.dto.ResponseUploadFileDTO;
import com.edwinkesuma.springedmastore.features.common.storage.infrastructure.cloudinary.CloudinaryStorageService;
import com.edwinkesuma.springedmastore.features.product.application.dto.RequestCreateCategoryDTO;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateCategoryUseCaseImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @Mock
    private CloudinaryStorageService cloudinaryStorageService;

    @InjectMocks
    private CreateCategoryUseCaseImpl createCategoryUseCase;

    @Test
    void execute_shouldCreateCategorySuccessfully() throws BadRequestException {

        RequestCreateCategoryDTO request =
                new RequestCreateCategoryDTO(
                        "Food",
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

        Category savedCategory = new Category();
        savedCategory.setIconUrl(
                "https://cloudinary.com/image.jpg"
        );
        savedCategory.setIconPublicId(
                "categories/image123"
        );

        ResponseUploadFileDTO uploadedFile =
                new ResponseUploadFileDTO(
                        "https://cloudinary.com/image.jpg",
                        "categories/image123"
                );

        ResponseCategoryDTO responseDTO =
                mock(ResponseCategoryDTO.class);

        when(categoryRepository.existsBySlug("food"))
                .thenReturn(false);

        when(categoryMapper.createCategoryDTOtoEntity(request))
                .thenReturn(category);

        when(cloudinaryStorageService.uploadFile(
                image,
                "categories"
        )).thenReturn(uploadedFile);

        when(categoryRepository.save(category))
                .thenReturn(savedCategory);

        when(categoryMapper.toCategoryDTO(savedCategory))
                .thenReturn(responseDTO);

        ResponseCategoryDTO result =
                createCategoryUseCase.execute(request, image);

        assertNotNull(result);
        assertEquals(responseDTO, result);

        assertEquals(
                "https://cloudinary.com/image.jpg",
                category.getIconUrl()
        );

        assertEquals(
                "categories/image123",
                category.getIconPublicId()
        );

        verify(categoryRepository)
                .existsBySlug("food");

        verify(cloudinaryStorageService)
                .uploadFile(image, "categories");

        verify(categoryRepository)
                .save(category);

        verify(categoryMapper)
                .toCategoryDTO(savedCategory);
    }

    @Test
    void execute_shouldThrowDuplicateResourceException_whenSlugAlreadyExists() {

        RequestCreateCategoryDTO request =
                new RequestCreateCategoryDTO(
                        "Food",
                        null,
                        1,
                        true
                );

        when(categoryRepository.existsBySlug("food"))
                .thenReturn(true);

        assertThrows(
                DuplicateResourceException.class,
                () -> createCategoryUseCase.execute(request, null)
        );

        verify(categoryRepository)
                .existsBySlug("food");

        verifyNoMoreInteractions(
                categoryMapper,
                cloudinaryStorageService
        );
    }

    @Test
    void execute_shouldThrowBadRequestException_whenImageIsNull() {

        RequestCreateCategoryDTO request =
                new RequestCreateCategoryDTO(
                        "Food",
                        null,
                        1,
                        true
                );

        when(categoryRepository.existsBySlug("food"))
                .thenReturn(false);

        assertThrows(
                BadRequestException.class,
                () -> createCategoryUseCase.execute(request, null)
        );

        verify(categoryRepository)
                .existsBySlug("food");

        verifyNoInteractions(
                categoryMapper,
                cloudinaryStorageService
        );
    }

    @Test
    void execute_shouldThrowBadRequestException_whenImageIsEmpty() {

        RequestCreateCategoryDTO request =
                new RequestCreateCategoryDTO(
                        "Food",
                        null,
                        1,
                        true
                );

        MultipartFile image = new MockMultipartFile(
                "image",
                "food.jpg",
                "image/jpeg",
                new byte[0]
        );

        when(categoryRepository.existsBySlug("food"))
                .thenReturn(false);

        assertThrows(
                BadRequestException.class,
                () -> createCategoryUseCase.execute(request, image)
        );
    }

    @Test
    void execute_shouldThrowBadRequestException_whenContentTypeIsInvalid() {

        RequestCreateCategoryDTO request =
                new RequestCreateCategoryDTO(
                        "Food",
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

        when(categoryRepository.existsBySlug("food"))
                .thenReturn(false);

        assertThrows(
                BadRequestException.class,
                () -> createCategoryUseCase.execute(request, image)
        );

        verifyNoInteractions(
                cloudinaryStorageService
        );
    }
}
