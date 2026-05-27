package com.edwinkesuma.springedmastore.features.product.application.usecase.impl.category;

import com.edwinkesuma.springedmastore.common.exception.DuplicateResourceException;
import com.edwinkesuma.springedmastore.common.exception.InvalidFileException;
import com.edwinkesuma.springedmastore.common.exception.ResourceNotFoundException;
import com.edwinkesuma.springedmastore.common.util.SlugUtil;
import com.edwinkesuma.springedmastore.features.common.storage.application.dto.ResponseUploadFileDTO;
import com.edwinkesuma.springedmastore.features.common.storage.application.service.FileStorageService;
import com.edwinkesuma.springedmastore.features.product.application.dto.RequestUpdateCategoryDTO;
import com.edwinkesuma.springedmastore.features.product.application.dto.ResponseCategoryDTO;
import com.edwinkesuma.springedmastore.features.product.application.mapper.CategoryMapper;
import com.edwinkesuma.springedmastore.features.product.application.usecase.category.UpdateCategoryUseCase;
import com.edwinkesuma.springedmastore.features.product.domain.entity.Category;
import com.edwinkesuma.springedmastore.features.product.domain.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UpdateCategoryUseCaseImpl implements UpdateCategoryUseCase {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final FileStorageService fileStorageService;

    @Override
    public ResponseCategoryDTO execute(RequestUpdateCategoryDTO request,
                                       UUID categoryId,
                                       MultipartFile image) {
        Category
                category =
                categoryRepository.findById(categoryId)
                        .orElseThrow(() -> new ResourceNotFoundException("Category", "id", categoryId));

        String normalizedName = request.name().trim();
        String newSlug = SlugUtil.toSlug(normalizedName);
        if (categoryRepository.existsBySlugAndIdNot(newSlug, categoryId)) {
            throw new DuplicateResourceException("Category", "name", normalizedName);
        }

        String oldIconPublicId = category.getIconPublicId();
        ResponseUploadFileDTO uploadedImage = null;

        if (image != null && !image.isEmpty()) {
            if (image.getContentType() == null || !image.getContentType().startsWith("image/")) {
                throw new InvalidFileException("Invalid image file");
            }

            uploadedImage = fileStorageService.uploadFile(image, "categories");

            category.setIconUrl(uploadedImage.imageUrl());
            category.setIconPublicId(uploadedImage.publicId());
        }

        category.setName(normalizedName);
        category.setSlug(newSlug);

        System.out.println(category.getIconUrl());
        Category savedCategory;
        try {
            savedCategory = categoryRepository.save(category);
        } catch (RuntimeException ex) {
            if (uploadedImage != null) {
                try {
                    fileStorageService.deleteFile(uploadedImage.publicId());
                } catch (RuntimeException cleanupEx) {
                    ex.addSuppressed(cleanupEx);
                }
            }
            throw ex;
        }

        if (uploadedImage != null && oldIconPublicId != null && !oldIconPublicId.isBlank()) {
            try {
                fileStorageService.deleteFile(oldIconPublicId);
            } catch (RuntimeException cleanupEx) {
                // log and continue
            }
        }
        return categoryMapper.toCategoryDTO(savedCategory);
    }
}
