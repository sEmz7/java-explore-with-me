package ru.yandex.practicum.statsdto;

import lombok.Data;

@Data
public class HitDto {
    private Long id;
    private String app;
    private String uri;
    private String ip;
    private String timestamp;
}
