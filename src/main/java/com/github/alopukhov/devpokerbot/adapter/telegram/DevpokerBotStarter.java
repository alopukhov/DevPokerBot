package com.github.alopukhov.devpokerbot.adapter.telegram;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.meta.generics.BotSession;

import javax.annotation.PreDestroy;
import javax.inject.Provider;

import static com.google.common.base.Preconditions.checkState;

@Service
@RequiredArgsConstructor
@Slf4j
public class DevpokerBotStarter implements SmartLifecycle {
    private final Provider<DevpokerBot> devpokerBotProvider;
    private final TelegramBotsApi botsApi;
    private BotSession botSession;

    @Override
    public synchronized boolean isRunning() {
        return botSession != null && botSession.isRunning();
    }

    @Override
    public synchronized void start() {
        checkState(botSession == null || !botSession.isRunning(), "Another instance already running");
        try {
            botSession = botsApi.registerBot(devpokerBotProvider.get());
        } catch (TelegramApiRequestException e) {
            log.error("Can't start bot");
            throw new RuntimeException("Can't start bot", e);
        }
    }

    @PreDestroy
    public synchronized void stop() {
        if (isRunning()) {
            botSession.stop();
            botSession = null;
        }
    }

    @Override
    public boolean isAutoStartup() {
        return true;
    }

    @Override
    public int getPhase() {
        return 0;
    }
}
