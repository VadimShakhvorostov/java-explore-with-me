package ru.practicum.main.service.categories;

import ru.practicum.main.dto.categories.CategoryRequest;
import ru.practicum.main.dto.categories.CategoryResponse;

import java.util.List;

public interface CategoryService {

    CategoryResponse addCategories(CategoryRequest categoryRequest);

    void deleteCategories(long catId);

    CategoryResponse updateCategories(long catId, CategoryRequest categoryRequest);

    List<CategoryResponse> getCategories(int from, int size);

    CategoryResponse getCategoriesById(long catId);
}
