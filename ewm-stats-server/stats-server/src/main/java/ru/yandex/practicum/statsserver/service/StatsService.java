package ru.yandex.practicum.statsserver.service;


import ru.yandex.practicum.statsdto.HitDto;

public interface StatsService {

    void saveHit(HitDto hitDto);
}
