package ru.yandex.practicum.ewmmainserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.ewmmainserver.model.participationRequest.RequestEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface RequestRepository extends JpaRepository<RequestEntity, Long> {
    Optional<RequestEntity> findByRequesterIdAndEventId(long requesterId, long eventId);

    List<RequestEntity> findAllByEventId(long eventId);

    List<RequestEntity> findAllByRequesterId(long requesterId);
}
