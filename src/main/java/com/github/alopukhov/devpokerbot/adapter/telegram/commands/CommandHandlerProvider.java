package com.github.alopukhov.devpokerbot.adapter.telegram.commands;

import com.github.alopukhov.devpokerbot.adapter.telegram.TelegramCommandHandler;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Optional;

@FunctionalInterface
public interface CommandHandlerProvider {
    Optional<TelegramCommandHandler> getHandler(Update update);
}
