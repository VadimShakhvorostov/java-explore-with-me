package ru.practicum.main.controller.publi;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.main.dto.categories.CategoriesResponse;
import ru.practicum.main.service.categories.CategoriesService;

import java.util.List;

@RestController
@RequestMapping
@AllArgsConstructor
public class PublicCategoriesController {

    private final CategoriesService categoriesService;


    @GetMapping("/categories")
    public List<CategoriesResponse> getCategories(
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "10") int size) {

        return categoriesService.getCategories(from, size);
    }

    @GetMapping("/categories/{catId}")
    public CategoriesResponse getCategoriesById(@PathVariable Long catId) {
        return categoriesService.getCategoriesById(catId);
    }


}
