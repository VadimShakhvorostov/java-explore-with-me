package ru.practicum.main.repositories.users;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    @Query("""
            select u from UserEntity u
            where(?1 is null or u.id in ?1)
            """)
    List<UserEntity> getUsersById(List<Long> ids, Pageable pageable);

    Boolean existsByEmailIgnoreCase(String email);
}
