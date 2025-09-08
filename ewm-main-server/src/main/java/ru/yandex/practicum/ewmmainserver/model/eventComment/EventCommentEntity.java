package ru.yandex.practicum.ewmmainserver.model.eventComment;

import jakarta.persistence.*;
import lombok.Data;
import ru.yandex.practicum.ewmmainserver.model.event.EventEntity;
import ru.yandex.practicum.ewmmainserver.model.event.EventState;
import ru.yandex.practicum.ewmmainserver.model.user.UserEntity;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "event_comments")
public class EventCommentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "event_id")
    private EventEntity event;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    private String text;

    @Column(name = "created_on")
    private LocalDateTime createdOn;

    @Enumerated(EnumType.STRING)
    private EventState status;
}
