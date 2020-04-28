package com.github.alopukhov.devpokerbot;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.telegram.telegrambots.ApiContextInitializer;

import java.time.Clock;

@EnableJpaRepositories
@SpringBootApplication
@EnableConfigurationProperties
@EnableCaching
public class DevpokerbotApplication {
    public static void main(String[] args) {
        ApiContextInitializer.init();
        SpringApplication.run(DevpokerbotApplication.class, args);
    }

    @Bean
    public Clock clock() {
        return Clock.systemDefaultZone();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
