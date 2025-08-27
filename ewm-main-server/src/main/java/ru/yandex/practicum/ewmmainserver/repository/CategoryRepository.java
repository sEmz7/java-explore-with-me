package ru.yandex.practicum.ewmmainserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.yandex.practicum.ewmmainserver.model.category.CategoryEntity;

import java.util.List;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {
    @Query(value = "SELECT * FROM categories ORDER BY id LIMIT :size OFFSET :from", nativeQuery = true)
    List<CategoryEntity> findAllFromBySize(@Param("from") int from, @Param("size") int size);
}
