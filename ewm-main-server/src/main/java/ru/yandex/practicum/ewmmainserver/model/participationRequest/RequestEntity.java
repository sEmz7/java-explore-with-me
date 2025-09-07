package ru.yandex.practicum.ewmmainserver.model.participationRequest;

import jakarta.persistence.*;
import lombok.Data;
import ru.yandex.practicum.ewmmainserver.model.event.EventEntity;
import ru.yandex.practicum.ewmmainserver.model.user.UserEntity;

import java.time.LocalDateTime;

@Entity
@Table(name = "requests")
@Data
public class RequestEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_date")
    private LocalDateTime created;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "event_id")
    private EventEntity event;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "requester_id")
    private UserEntity requester;

    @Enumerated(EnumType.STRING)
    private RequestStatus status;
}
