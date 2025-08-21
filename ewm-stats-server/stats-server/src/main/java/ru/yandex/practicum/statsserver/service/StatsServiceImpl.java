package ru.yandex.practicum.statsserver.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.statsdto.HitDto;
import ru.yandex.practicum.statsserver.model.HitMapper;
import ru.yandex.practicum.statsserver.repository.StatsRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {
    private final HitMapper hitMapper;
    private final StatsRepository statsRepository;

    @Override
    public void saveHit(HitDto hitDto) {
        var hitEntity = hitMapper.toEntity(hitDto);
        statsRepository.save(hitEntity);
    }
}
