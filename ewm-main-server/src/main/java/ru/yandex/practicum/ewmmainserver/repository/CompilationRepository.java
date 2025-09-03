package ru.yandex.practicum.ewmmainserver.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.ewmmainserver.model.compilation.CompilationEntity;

@Repository
public interface CompilationRepository extends JpaRepository<CompilationEntity, Long> {

    @Query("SELECT c FROM CompilationEntity c " +
            "WHERE (:pinned IS NULL OR c.pinned = :pinned)")
    Page<CompilationEntity> findCompilations(Boolean pinned, Pageable pageable);
}
