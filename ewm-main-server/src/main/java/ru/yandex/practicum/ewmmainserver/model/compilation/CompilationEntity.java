package ru.yandex.practicum.ewmmainserver.model.compilation;

import jakarta.persistence.*;
import lombok.Data;
import ru.yandex.practicum.ewmmainserver.model.event.EventEntity;

import java.util.Set;

@Entity
@Data
@Table(name = "compilations")
public class CompilationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "compilation_event",
            joinColumns = @JoinColumn(name = "compilation_id"),
            inverseJoinColumns = @JoinColumn(name = "event_id")
    )
    private Set<EventEntity> events;

    private Boolean pinned;

    private String title;
}
