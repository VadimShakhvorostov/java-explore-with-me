package ru.practicum.main.service.categories;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.main.dto.categories.CategoriesRequest;
import ru.practicum.main.dto.categories.CategoriesResponse;
import ru.practicum.main.dto.categories.mapper.CategoriesMapper;
import ru.practicum.main.exception.NotFoundException;
import ru.practicum.main.exception.RequestException;
import ru.practicum.main.repositories.categories.CategoriesEntity;
import ru.practicum.main.repositories.categories.CategoriesRepository;
import ru.practicum.main.repositories.events.EventsRepository;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class CategoriesServiceImpl implements CategoriesService {

    private final CategoriesRepository categoriesRepository;
    private final CategoriesMapper categoriesMapper;
    private final EventsRepository eventsRepository;

    @Override
    public CategoriesResponse addCategories(CategoriesRequest categoriesRequest) {
        if (categoriesRepository.existsByName(categoriesRequest.getName())) {
            throw new RequestException("name exists");
        }
        CategoriesEntity categoriesEntity = categoriesMapper.toCategoriesEntity(categoriesRequest);
        return categoriesMapper.toCategoriesResponse(categoriesRepository.save(categoriesEntity));
    }

    @Override
    public void deleteCategories(long catId) {
        validateId(catId);
        if (eventsRepository.existsByCategory_Id(catId)) {
            throw new RequestException("categories using");
        }
        categoriesRepository.deleteById(catId);
    }

    @Override
    public CategoriesResponse updateCategories(long catId, CategoriesRequest categoriesRequest) {
        if (categoriesRepository.existsByNameAndIdNot(categoriesRequest.getName(), catId)) {
            throw new RequestException("name exists");
        }
        CategoriesEntity categoriesEntity = categoriesRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("not found"));
        categoriesEntity.setName(categoriesRequest.getName());
        return categoriesMapper.toCategoriesResponse(categoriesRepository.save(categoriesEntity));
    }

    private void validateId(long catId) {
        log.info("validateId, catId {}", catId);
        categoriesRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Categories with id " + catId + " not found"));
    }

    @Override
    public List<CategoriesResponse> getCategories(int from, int size) {

        return categoriesRepository.findAll(PageRequest.of(from, size))
                .map(categoriesMapper::toCategoriesResponse)
                .toList();
    }

    @Override
    public CategoriesResponse getCategoriesById(long catId) {
        validateId(catId);
        return categoriesMapper.toCategoriesResponse(categoriesRepository.findById(catId).get());
    }
}
