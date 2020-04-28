package com.github.alopukhov.devpokerbot.adapter.telegram;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

public interface TelegramCommandHandler {
    void handle(Update update, AbsSender callback);
}
