package ru.yandex.practicum.statsserver.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.statsdto.EndpointHit;
import ru.yandex.practicum.statsdto.ViewStats;
import ru.yandex.practicum.statsserver.exception.InvalidUserInputException;
import ru.yandex.practicum.statsserver.model.HitMapper;
import ru.yandex.practicum.statsserver.repository.StatsRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {
    private final HitMapper hitMapper;
    private final StatsRepository statsRepository;

    @Override
    public void saveHit(EndpointHit endpointHit) {
        var hitEntity = hitMapper.toEntity(endpointHit);
        statsRepository.save(hitEntity);
    }

    @Override
    public List<ViewStats> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        if (end.isBefore(start)) {
            throw new InvalidUserInputException("Дата end не может быть раньше start.");
        }
        if (unique) {
            return statsRepository.findUniqueStats(start, end, uris);
        } else {
            return statsRepository.findAllStats(start, end, uris);
        }
    }
}
