package com.edwinkesuma.springedmastore.features.product.application.mapper;

import com.edwinkesuma.springedmastore.features.product.application.dto.RequestCreateCategoryDTO;
import com.edwinkesuma.springedmastore.features.product.application.dto.ResponseCategoryDTO;
import com.edwinkesuma.springedmastore.features.product.domain.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CategoryMapper {
    ResponseCategoryDTO toCategoryDTO(Category category);

    Category createCategoryDTOtoEntity(RequestCreateCategoryDTO request);
}
