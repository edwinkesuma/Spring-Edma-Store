package com.edwinkesuma.springedmastore.features.product.application.usecase.impl.category;

import com.edwinkesuma.springedmastore.common.exception.ResourceNotFoundException;
import com.edwinkesuma.springedmastore.features.product.application.dto.ResponseCategoryDTO;
import com.edwinkesuma.springedmastore.features.product.application.mapper.CategoryMapper;
import com.edwinkesuma.springedmastore.features.product.domain.entity.Category;
import com.edwinkesuma.springedmastore.features.product.domain.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetDetailCategoryUseCaseImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private GetDetailCategoryUseCaseImpl getDetailCategoryUseCase;

    @Test
    void execute_shouldReturnResponseCategoryDTO_whenCategoryExists() {

        UUID categoryId = UUID.randomUUID();

        Category category = new Category();
        category.setName("Food");

        ResponseCategoryDTO responseDTO =
                mock(ResponseCategoryDTO.class);

        when(categoryRepository.findById(categoryId))
                .thenReturn(Optional.of(category));

        when(categoryMapper.toCategoryDTO(category))
                .thenReturn(responseDTO);

        ResponseCategoryDTO result =
                getDetailCategoryUseCase.execute(categoryId);

        assertNotNull(result);
        assertEquals(responseDTO, result);

        verify(categoryRepository).findById(categoryId);
        verify(categoryMapper).toCategoryDTO(category);
    }

    @Test
    void execute_shouldThrowResourceNotFoundException_whenCategoryNotFound() {

        UUID categoryId = UUID.randomUUID();

        when(categoryRepository.findById(categoryId))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> getDetailCategoryUseCase.execute(categoryId)
        );

        verify(categoryRepository).findById(categoryId);
        verifyNoInteractions(categoryMapper);
    }
}
