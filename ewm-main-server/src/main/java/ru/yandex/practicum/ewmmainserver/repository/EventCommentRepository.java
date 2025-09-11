package ru.yandex.practicum.ewmmainserver.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.ewmmainserver.model.event.EventState;
import ru.yandex.practicum.ewmmainserver.model.eventComment.EventCommentEntity;

import java.util.Optional;

public interface EventCommentRepository extends JpaRepository<EventCommentEntity, Long> {
    Optional<EventCommentEntity> findByEventIdAndUserId(long eventId, long userId);

    Page<EventCommentEntity> findAllByEventIdAndStatus(long eventId, EventState status, Pageable pageable);

    EventCommentEntity findByIdAndStatus(long id, EventState status);
}
