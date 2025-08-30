package ru.yandex.practicum.ewmmainserver.model.event;

import jakarta.persistence.*;
import lombok.Data;
import ru.yandex.practicum.ewmmainserver.model.category.CategoryEntity;
import ru.yandex.practicum.ewmmainserver.model.user.UserEntity;

import java.time.LocalDateTime;

@Entity
@Table(name = "events")
@Data
public class EventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 2000, nullable = false)
    private String annotation;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private CategoryEntity category;

    @Column(name = "confirmed_requests")
    private Integer confirmedRequests = 0;

    @Column(name = "created_on")
    private LocalDateTime createdOn;

    @Column(length = 7000, nullable = false)
    private String description;

    @Column(name = "event_date")
    private LocalDateTime eventDate;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "initiator_id")
    private UserEntity initiator;

    @Column(name = "location_lat")
    private Float locationLat;

    @Column(name = "location_lon")
    private Float locationLon;

    private Boolean paid = false;

    @Column(name = "participant_limit")
    private Integer participantLimit = 0;

    @Column(name = "published_on")
    private LocalDateTime publishedOn;

    @Column(name = "request_moderation")
    private Boolean requestModeration = true;

    @Enumerated(EnumType.STRING)
    private EventState state = EventState.WAITING;

    @Column(length = 255, nullable = false)
    private String title;
}
