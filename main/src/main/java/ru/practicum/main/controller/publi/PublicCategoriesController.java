package ru.practicum.main.controller.publi;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.main.dto.categories.CategoryResponse;
import ru.practicum.main.service.categories.CategoryService;

import java.util.List;

@RestController
@RequestMapping("/categories")
@AllArgsConstructor
public class PublicCategoriesController {

    private final CategoryService categoryService;


    @GetMapping
    public List<CategoryResponse> getCategories(
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "10") @PositiveOrZero int size) {

        return categoryService.getCategories(from, size);
    }

    @GetMapping("/{catId}")
    public CategoryResponse getCategoriesById(@PathVariable Long catId) {
        return categoryService.getCategoriesById(catId);
    }


}
