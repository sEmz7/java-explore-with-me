package ru.yandex.practicum.ewmmainserver.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.ewmmainserver.model.event.EventEntity;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<EventEntity, Long> {
    List<EventEntity> findAllByInitiatorId(long userId, Pageable pageable);

    Boolean existsByCategoryId(long catId);
}
