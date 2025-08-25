package ru.yandex.practicum.statsserver.service;


import ru.yandex.practicum.statsdto.EndpointHit;
import ru.yandex.practicum.statsdto.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {

    void saveHit(EndpointHit endpointHit);

    List<ViewStats> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique);
}
