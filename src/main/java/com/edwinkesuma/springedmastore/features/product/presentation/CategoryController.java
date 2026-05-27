package com.edwinkesuma.springedmastore.features.product.presentation;

import com.edwinkesuma.springedmastore.features.product.application.dto.ResponseCategoryDTO;
import com.edwinkesuma.springedmastore.features.product.application.usecase.category.GetAllCategoryUseCase;
import com.edwinkesuma.springedmastore.features.product.application.usecase.category.GetDetailCategoryUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final GetAllCategoryUseCase getAllCategoryUseCase;
    private final GetDetailCategoryUseCase getDetailCategoryUseCase;

    @GetMapping
    public ResponseEntity<List<ResponseCategoryDTO>> toCategoryDTO() {
        List<ResponseCategoryDTO> categories = getAllCategoryUseCase.execute();

        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<ResponseCategoryDTO> getDetailCategoryDTO(@PathVariable UUID categoryId) {
        ResponseCategoryDTO category = getDetailCategoryUseCase.execute(categoryId);

        return ResponseEntity.ok(category);
    }
}
