package ru.yandex.practicum.ewmmainserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.ewmmainserver.model.participationRequest.RequestEntity;
import ru.yandex.practicum.ewmmainserver.model.participationRequest.RequestStatus;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<RequestEntity, Long> {
    Optional<RequestEntity> findByRequesterIdAndEventId(long requesterId, long eventId);

    List<RequestEntity> findAllByEventId(long eventId);

    List<RequestEntity> findAllByEventIdAndStatus(long eventId, RequestStatus status);

    List<RequestEntity> findAllByRequesterId(long requesterId);

    List<RequestEntity> findAllByIdIn(List<Long> requestIds);
}
