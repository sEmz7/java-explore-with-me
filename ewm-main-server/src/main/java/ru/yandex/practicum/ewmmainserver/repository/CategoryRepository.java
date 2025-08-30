package ru.yandex.practicum.ewmmainserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.ewmmainserver.model.category.CategoryEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {
    @Query(value = "SELECT * FROM categories ORDER BY id LIMIT :size OFFSET :from", nativeQuery = true)
    List<CategoryEntity> findAllFromBySize(@Param("from") int from, @Param("size") int size);

    Optional<CategoryEntity> findByName(String name);
}
