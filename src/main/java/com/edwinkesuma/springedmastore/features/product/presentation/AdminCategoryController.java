package com.edwinkesuma.springedmastore.features.product.presentation;

import com.edwinkesuma.springedmastore.features.product.application.dto.RequestCreateCategoryDTO;
import com.edwinkesuma.springedmastore.features.product.application.dto.RequestUpdateCategoryDTO;
import com.edwinkesuma.springedmastore.features.product.application.dto.ResponseCategoryDTO;
import com.edwinkesuma.springedmastore.features.product.application.usecase.category.CreateCategoryUseCase;
import com.edwinkesuma.springedmastore.features.product.application.usecase.category.DeleteCategoryUseCase;
import com.edwinkesuma.springedmastore.features.product.application.usecase.category.UpdateCategoryUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin/categories")
@RequiredArgsConstructor
public class AdminCategoryController {

    private final CreateCategoryUseCase createCategoryUseCase;
    private final UpdateCategoryUseCase updateCategoryUseCase;
    private final DeleteCategoryUseCase deleteCategoryUseCase;

    @PostMapping
    public ResponseEntity<ResponseCategoryDTO> createCategory(@Valid @RequestPart("category") RequestCreateCategoryDTO categoryDTO,
                                                              @RequestPart("image") MultipartFile image
    ) {
        ResponseCategoryDTO response = createCategoryUseCase.execute(categoryDTO, image);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<ResponseCategoryDTO> updateCategory(@Valid @RequestPart("category") RequestUpdateCategoryDTO categoryDTO,
                                                              @RequestPart(value = "image", required = false) MultipartFile image,
                                                              @PathVariable UUID categoryId
    ) {
        ResponseCategoryDTO response = updateCategoryUseCase.execute(categoryDTO, categoryId, image);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Void> deleteCategory(@PathVariable UUID categoryId) {
        deleteCategoryUseCase.execute(categoryId);
        return ResponseEntity.noContent().build();
    }
}
