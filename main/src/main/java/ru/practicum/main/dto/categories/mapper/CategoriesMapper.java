package ru.practicum.main.dto.categories.mapper;

import org.mapstruct.Mapper;
import ru.practicum.main.dto.categories.CategoriesRequest;
import ru.practicum.main.dto.categories.CategoriesResponse;
import ru.practicum.main.repositories.categories.CategoriesEntity;

@Mapper(componentModel = "spring")
public interface CategoriesMapper {

    CategoriesEntity toCategoriesEntity(CategoriesRequest categoriesRequest);

    CategoriesResponse toCategoriesResponse(CategoriesEntity categoriesEntity);
}
