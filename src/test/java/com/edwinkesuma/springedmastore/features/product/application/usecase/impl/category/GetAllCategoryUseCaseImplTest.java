package com.edwinkesuma.springedmastore.features.product.application.usecase.impl.category;

import com.edwinkesuma.springedmastore.features.product.application.dto.ResponseCategoryDTO;
import com.edwinkesuma.springedmastore.features.product.application.mapper.CategoryMapper;
import com.edwinkesuma.springedmastore.features.product.domain.entity.Category;
import com.edwinkesuma.springedmastore.features.product.domain.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetAllCategoryUseCasImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private GetAllCategoryUseCaseImpl getAllCategoryUseCas;

    @Test
    void execute_shouldReturnListOfResponseCategoryDTO() {

        Category category1 = new Category();
        category1.setName("Food");

        Category category2 = new Category();
        category2.setName("Drink");

        List<Category> categories = List.of(category1, category2);

        ResponseCategoryDTO response1 = mock(ResponseCategoryDTO.class);
        ResponseCategoryDTO response2 = mock(ResponseCategoryDTO.class);

        when(categoryRepository.findAll())
                .thenReturn(categories);

        when(categoryMapper.toCategoryDTO(category1))
                .thenReturn(response1);

        when(categoryMapper.toCategoryDTO(category2))
                .thenReturn(response2);

        List<ResponseCategoryDTO> result =
                getAllCategoryUseCas.execute();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(response1, result.get(0));
        assertEquals(response2, result.get(1));

        verify(categoryRepository).findAll();

        verify(categoryMapper).toCategoryDTO(category1);
        verify(categoryMapper).toCategoryDTO(category2);
    }

    @Test
    void execute_shouldReturnEmptyList_whenNoCategoriesFound() {

        when(categoryRepository.findAll())
                .thenReturn(List.of());

        List<ResponseCategoryDTO> result =
                getAllCategoryUseCas.execute();

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(categoryRepository).findAll();
        verifyNoInteractions(categoryMapper);
    }
}
