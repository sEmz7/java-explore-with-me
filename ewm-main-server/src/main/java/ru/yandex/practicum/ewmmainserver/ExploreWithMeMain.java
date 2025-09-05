package ru.yandex.practicum.ewmmainserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
        "ru.yandex.practicum.ewmmainserver",
        "ru.yandex.practicum.statsclient"
})
public class ExploreWithMeMain {

    public static void main(String[] args) {
        SpringApplication.run(ExploreWithMeMain.class, args);
    }

}
