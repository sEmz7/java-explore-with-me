package ru.yandex.practicum.ewmmainserver.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.ewmmainserver.model.event.EventEntity;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<EventEntity, Long> {
    List<EventEntity> findAllByInitiatorId(long userId, Pageable pageable);

    Boolean existsByCategoryId(long catId);

    @Query("SELECT e FROM EventEntity e " +
            "WHERE (:users IS NULL OR e.initiator.id IN :users) " +
            "AND (:states IS NULL OR e.state IN :states) " +
            "AND (:categories IS NULL OR e.category.id IN :categories) " +
            "AND (cast(:rangeStart as date) IS NULL OR e.eventDate >= :rangeStart) " +
            "AND (cast(:rangeEnd as date) IS NULL OR e.eventDate <= :rangeEnd)")
    Page<EventEntity> searchEvents(@Param("users") List<Long> users,
                                         @Param("states") List<String> states,
                                         @Param("categories") List<Long> categories,
                                         @Param("rangeStart") LocalDateTime rangeStart,
                                         @Param("rangeEnd") LocalDateTime rangeEnd,
                                         Pageable pageable);

    @Query("SELECT e FROM EventEntity e " +
            "LEFT JOIN RequestEntity r ON r.status = 'CONFIRMED' AND r.event.id = e.id " +
            "WHERE (:text IS NULL OR e.annotation ilike %:text% OR e.description ilike %:text%) " +
            "AND (:categories IS NULL OR e.category.id IN :categories) " +
            "AND (:paid IS NULL OR e.paid = :paid) " +
            "AND (cast(:rangeStart as date) IS NULL OR e.eventDate >= :rangeStart) " +
            "AND (cast(:rangeEnd as date) IS NULL OR e.eventDate <= :rangeEnd) " +
            "GROUP BY e " +
            "HAVING (:onlyAvailable = false OR COUNT(r.id) < e.participantLimit OR e.participantLimit = 0)")
    Page<EventEntity> getEvents(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart,
                                LocalDateTime rangeEnd, Boolean onlyAvailable, Pageable pageable);
}
