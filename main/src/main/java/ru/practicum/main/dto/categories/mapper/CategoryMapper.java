package ru.practicum.main.dto.categories.mapper;

import org.mapstruct.Mapper;
import ru.practicum.main.dto.categories.CategoryRequest;
import ru.practicum.main.dto.categories.CategoryResponse;
import ru.practicum.main.repositories.categories.CategoryEntity;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryEntity toCategoriesEntity(CategoryRequest categoryRequest);

    CategoryResponse toCategoriesResponse(CategoryEntity categoryEntity);
}
