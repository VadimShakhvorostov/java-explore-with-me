package ru.practicum.main.service.categories;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.main.dto.categories.CategoryRequest;
import ru.practicum.main.dto.categories.CategoryResponse;
import ru.practicum.main.dto.categories.mapper.CategoryMapper;
import ru.practicum.main.exception.ConstraintException;
import ru.practicum.main.exception.NotFoundException;
import ru.practicum.main.exception.RequestException;
import ru.practicum.main.repositories.categories.CategoryEntity;
import ru.practicum.main.repositories.categories.CategoryRepository;
import ru.practicum.main.repositories.events.EventsRepository;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final EventsRepository eventsRepository;

    @Override
    @Transactional
    public CategoryResponse addCategories(CategoryRequest categoryRequest) {
        if (categoryRepository.existsByName(categoryRequest.getName())) {
            throw new ConstraintException("Integrity constraint has been violated.");
        }
        CategoryEntity categoryEntity = categoryMapper.toCategoriesEntity(categoryRequest);
        return categoryMapper.toCategoriesResponse(categoryRepository.save(categoryEntity));
    }

    @Override
    @Transactional
    public void deleteCategories(long catId) {
        validateId(catId);
        if (eventsRepository.existsByCategory_Id(catId)) {
            throw new RequestException("The category is not empty");
        }
        categoryRepository.deleteById(catId);
    }

    @Override
    @Transactional
    public CategoryResponse updateCategories(long catId, CategoryRequest categoryRequest) {
        CategoryEntity categoryEntity = categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Categories with id=" + catId + " not found"));
        categoryEntity.setName(categoryRequest.getName());
        return categoryMapper.toCategoriesResponse(categoryRepository.save(categoryEntity));
    }

    private void validateId(long catId) {
        log.info("validateId, catId {}", catId);
        categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Categories with id=" + catId + " not found"));
    }

    @Override
    public List<CategoryResponse> getCategories(int from, int size) {
        return categoryRepository.findAll(PageRequest.of(from, size))
                .map(categoryMapper::toCategoriesResponse)
                .toList();
    }

    @Override
    public CategoryResponse getCategoriesById(long catId) {
        validateId(catId);
        return categoryMapper.toCategoriesResponse(categoryRepository.findById(catId).get());
    }
}
