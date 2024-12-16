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
import ru.practicum.main.dto.categories.CategoriesRequest;
import ru.practicum.main.dto.categories.CategoriesResponse;
import ru.practicum.main.service.categories.CategoriesService;

@RestController
@RequestMapping("/admin")
@AllArgsConstructor
public class AdminCategoriesController {

    private final CategoriesService categoriesService;

    @PostMapping("/categories")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoriesResponse addCategories(@RequestBody @Valid CategoriesRequest categoriesDto) {
        return categoriesService.addCategories(categoriesDto);
    }

    @DeleteMapping("/categories/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategories(@PathVariable long catId) {
        categoriesService.deleteCategories(catId);
    }

    @PatchMapping("/categories/{catId}")
    public CategoriesResponse updateCategories(
            @PathVariable long catId,
            @RequestBody @Valid CategoriesRequest categoriesRequest) {
        return categoriesService.updateCategories(catId, categoriesRequest);
    }
}
