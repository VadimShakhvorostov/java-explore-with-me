package ru.practicum.main.repositories.compilation;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompilationsRepository extends JpaRepository<CompilationEntity, Long> {

    List<CompilationEntity> findByPinned(boolean pinned, Pageable pageable);
}
