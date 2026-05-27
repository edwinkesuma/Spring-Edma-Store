package com.edwinkesuma.springedmastore.features.product.application.usecase.impl.category;

import com.edwinkesuma.springedmastore.common.exception.DuplicateResourceException;
import com.edwinkesuma.springedmastore.common.exception.InvalidFileException;
import com.edwinkesuma.springedmastore.common.util.SlugUtil;
import com.edwinkesuma.springedmastore.features.common.storage.application.dto.ResponseUploadFileDTO;
import com.edwinkesuma.springedmastore.features.common.storage.application.service.FileStorageService;
import com.edwinkesuma.springedmastore.features.product.application.dto.RequestCreateCategoryDTO;
import com.edwinkesuma.springedmastore.features.product.application.dto.ResponseCategoryDTO;
import com.edwinkesuma.springedmastore.features.product.application.mapper.CategoryMapper;
import com.edwinkesuma.springedmastore.features.product.application.usecase.category.CreateCategoryUseCase;
import com.edwinkesuma.springedmastore.features.product.domain.entity.Category;
import com.edwinkesuma.springedmastore.features.product.domain.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class CreateCategoryUseCaseImpl implements CreateCategoryUseCase {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    private final FileStorageService fileStorageService;

    @Override
    public ResponseCategoryDTO execute(RequestCreateCategoryDTO request,
                                       MultipartFile image) {

        String slug = SlugUtil.toSlug(request.name());

        if (categoryRepository.existsBySlug(slug)) {
            throw new DuplicateResourceException("Category", "name", request.name());
        }

        if (image == null || image.isEmpty()) {
            throw new InvalidFileException("Category image is required");
        }

        if (image.getContentType() == null || !image.getContentType().startsWith("image/")) {
            throw new InvalidFileException("Invalid image file");
        }

        Category category = categoryMapper.createCategoryDTOtoEntity(request);

        ResponseUploadFileDTO uploadedImage = fileStorageService.uploadFile(image, "categories");
        category.setIconUrl(uploadedImage.imageUrl());
        category.setIconPublicId(uploadedImage.publicId());
        category.setSlug(slug);


        Category savedCategory = categoryRepository.save(category);

        return categoryMapper.toCategoryDTO(savedCategory);
    }
}
