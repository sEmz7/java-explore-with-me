package ru.yandex.practicum.statsserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.statsserver.model.Hit;

public interface StatsRepository extends JpaRepository<Hit, Long> {

}