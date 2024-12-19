package ru.practicum.main.controller.admin;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.main.dto.categories.CategoryRequest;
import ru.practicum.main.dto.categories.CategoryResponse;
import ru.practicum.main.service.categories.CategoryService;

@RestController
@RequestMapping("/admin/categories")
@AllArgsConstructor
public class AdminCategoryController {

    private final CategoryService categoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryResponse addCategories(@RequestBody @Valid CategoryRequest categoriesDto) {
        return categoryService.addCategories(categoriesDto);
    }

    @DeleteMapping("/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategories(@PathVariable long catId) {
        categoryService.deleteCategories(catId);
    }

    @PatchMapping("/{catId}")
    public CategoryResponse updateCategories(
            @PathVariable long catId,
            @RequestBody @Valid CategoryRequest categoryRequest) {
        return categoryService.updateCategories(catId, categoryRequest);
    }
}
