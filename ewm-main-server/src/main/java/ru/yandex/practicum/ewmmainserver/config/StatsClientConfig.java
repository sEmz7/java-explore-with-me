package ru.yandex.practicum.ewmmainserver.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.yandex.practicum.statsclient.StatsClient;

@Configuration
public class StatsClientConfig {
    @Bean
    public StatsClient statsClient(@Value("${stats-server.url:http://localhost:9090}") String serverUrl,
                                   RestTemplateBuilder builder) {
        return new StatsClient(serverUrl, builder);
    }
}
