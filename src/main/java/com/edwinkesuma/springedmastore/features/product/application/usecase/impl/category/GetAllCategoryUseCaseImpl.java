package com.edwinkesuma.springedmastore.features.product.application.usecase.impl.category;

import com.edwinkesuma.springedmastore.features.product.application.dto.ResponseCategoryDTO;
import com.edwinkesuma.springedmastore.features.product.application.mapper.CategoryMapper;
import com.edwinkesuma.springedmastore.features.product.application.usecase.category.GetAllCategoryUseCase;
import com.edwinkesuma.springedmastore.features.product.domain.entity.Category;
import com.edwinkesuma.springedmastore.features.product.domain.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetAllCategoryUseCaseImpl implements GetAllCategoryUseCase {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public List<ResponseCategoryDTO> execute() {
        List<Category> categories = categoryRepository.findAll();

        return categories.stream().map(categoryMapper::toCategoryDTO).toList();
    }
}
