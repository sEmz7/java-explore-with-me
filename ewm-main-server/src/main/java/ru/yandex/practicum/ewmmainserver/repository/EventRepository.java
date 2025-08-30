package ru.yandex.practicum.ewmmainserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.ewmmainserver.model.event.EventEntity;

@Repository
public interface EventRepository extends JpaRepository<EventEntity, Long> {
}
