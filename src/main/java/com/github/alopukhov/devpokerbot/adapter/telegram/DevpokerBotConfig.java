package com.github.alopukhov.devpokerbot.adapter.telegram;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.ApiContext;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

@Configuration
public class DevpokerBotConfig {
    @Bean
    public TelegramBotsApi telegramBotsApi() {
        return new TelegramBotsApi();
    }

    @Bean
    @Scope(SCOPE_PROTOTYPE)
    public DevpokerBot telegramBot(DevpokerBotProperties botProperties, TelegramUpdateRouter router) {
        DefaultBotOptions options = ApiContext.getInstance(DefaultBotOptions.class);
        if (botProperties.getProxy().isEnabled()) {
            options.setProxyHost(botProperties.getProxy().getHost());
            options.setProxyPort(botProperties.getProxy().getPort());
            options.setProxyType(botProperties.getProxy().getType());
        }
        return DevpokerBot.builder()
                .botToken(botProperties.getToken())
                .botUserName(botProperties.getName())
                .router(router)
                .options(options)
                .build();
    }
}
