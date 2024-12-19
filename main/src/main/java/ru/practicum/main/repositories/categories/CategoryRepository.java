package ru.practicum.main.repositories.categories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {

    Boolean existsByNameAndIdNot(String name, Long id);

    Boolean existsByName(String name);
}
