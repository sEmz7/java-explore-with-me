package ru.yandex.practicum.statsclient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriComponentsBuilder;
import ru.yandex.practicum.statsdto.EndpointHit;
import ru.yandex.practicum.statsdto.ViewStats;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class StatsClient extends BaseClientStats {
    private static final String HIT_URL = "/hit";
    private static final String GET_STATS_URL = "/stats";
    private final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public StatsClient(@Value("${stats-server.url:http://localhost:9090}") String serverUrl, RestTemplateBuilder builder) {
        super(builder.uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                .build());
    }

    public void hit(EndpointHit endpointHit) {
        post(HIT_URL, null, endpointHit, Void.class);
    }

    public List<ViewStats> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromPath(GET_STATS_URL)
                .queryParam("start", start.format(formatter))
                .queryParam("end", end.format(formatter))
                .queryParam("unique", unique);

        if (uris != null) {
            for (String uri : uris) {
                builder.queryParam("uris", uri);
            }
        }

        ResponseEntity<ViewStats[]> response = get(builder.toUriString(), null, ViewStats[].class);
        return List.of(response.getBody());
    }
}
