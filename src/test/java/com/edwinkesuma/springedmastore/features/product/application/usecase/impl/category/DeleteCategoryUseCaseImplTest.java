package com.edwinkesuma.springedmastore.features.product.application.usecase.impl.category;

import com.edwinkesuma.springedmastore.common.exception.ResourceNotFoundException;
import com.edwinkesuma.springedmastore.features.common.storage.infrastructure.cloudinary.CloudinaryStorageService;
import com.edwinkesuma.springedmastore.features.product.domain.entity.Category;
import com.edwinkesuma.springedmastore.features.product.domain.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteCategoryUseCaseImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CloudinaryStorageService cloudinaryStorageService;

    @InjectMocks
    private DeleteCategoryUseCaseImpl deleteCategoryUseCase;

    @Test
    void execute_shouldDeleteCategorySuccessfully_whenIconPublicIdExists() {

        UUID categoryId = UUID.randomUUID();

        Category category = new Category();
        category.setIconPublicId("categories/image123");

        when(categoryRepository.findById(categoryId))
                .thenReturn(Optional.of(category));

        deleteCategoryUseCase.execute(categoryId);

        verify(categoryRepository)
                .findById(categoryId);

        verify(cloudinaryStorageService)
                .deleteFile("categories/image123");

        verify(categoryRepository)
                .delete(category);
    }

    @Test
    void execute_shouldDeleteCategorySuccessfully_whenIconPublicIdIsNull() {

        UUID categoryId = UUID.randomUUID();

        Category category = new Category();
        category.setIconPublicId(null);

        when(categoryRepository.findById(categoryId))
                .thenReturn(Optional.of(category));

        deleteCategoryUseCase.execute(categoryId);

        verify(categoryRepository)
                .findById(categoryId);

        verify(cloudinaryStorageService, never())
                .deleteFile(anyString());

        verify(categoryRepository)
                .delete(category);
    }

    @Test
    void execute_shouldThrowResourceNotFoundException_whenCategoryNotFound() {

        UUID categoryId = UUID.randomUUID();

        when(categoryRepository.findById(categoryId))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> deleteCategoryUseCase.execute(categoryId)
        );

        verify(categoryRepository)
                .findById(categoryId);

        verifyNoInteractions(cloudinaryStorageService);

        verify(categoryRepository, never())
                .delete(any(Category.class));
    }
}
