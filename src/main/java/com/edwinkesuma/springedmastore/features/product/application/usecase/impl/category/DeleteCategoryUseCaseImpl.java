package com.edwinkesuma.springedmastore.features.product.application.usecase.impl.category;

import com.edwinkesuma.springedmastore.common.exception.ResourceNotFoundException;
import com.edwinkesuma.springedmastore.features.common.storage.application.service.FileStorageService;
import com.edwinkesuma.springedmastore.features.product.application.usecase.category.DeleteCategoryUseCase;
import com.edwinkesuma.springedmastore.features.product.domain.entity.Category;
import com.edwinkesuma.springedmastore.features.product.domain.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeleteCategoryUseCaseImpl implements DeleteCategoryUseCase {

    private final CategoryRepository categoryRepository;
    private final FileStorageService fileStorageService;

    @Override
    public void execute(UUID id) {
        Category
                category =
                categoryRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));

//        if (productRepository.existsByCategoryId(id)) {
//            throw new BadRequestException(
//                    "Category still has products"
//            );
//        }

        String iconPublicId = category.getIconPublicId();
        categoryRepository.delete(category);

        if (iconPublicId != null && !iconPublicId.isBlank()) {
            fileStorageService.deleteFile(iconPublicId);
        }
    }
}
