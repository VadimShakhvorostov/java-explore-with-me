package ru.practicum.main.service.categories;

import ru.practicum.main.dto.categories.CategoriesRequest;
import ru.practicum.main.dto.categories.CategoriesResponse;

import java.util.List;

public interface CategoriesService {

    CategoriesResponse addCategories(CategoriesRequest categoriesRequest);

    void deleteCategories(long catId);

    CategoriesResponse updateCategories(long catId, CategoriesRequest categoriesRequest);

    List<CategoriesResponse> getCategories(int from, int size);

    CategoriesResponse getCategoriesById(long catId);
}
