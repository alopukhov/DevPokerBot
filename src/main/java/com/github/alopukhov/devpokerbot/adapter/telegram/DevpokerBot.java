package com.github.alopukhov.devpokerbot.adapter.telegram;

import lombok.Builder;
import lombok.NonNull;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

public class DevpokerBot extends TelegramLongPollingBot {
    private final String botToken;
    private final String botUserName;
    private final TelegramUpdateRouter router;

    @Builder
    public DevpokerBot(@NonNull String botToken, @NonNull String botUserName, @NonNull TelegramUpdateRouter router, @NonNull DefaultBotOptions options) {
        super(options);
        this.botToken = botToken;
        this.botUserName = botUserName;
        this.router = router;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public String getBotUsername() {
        return botUserName;
    }

    @Override
    public void onUpdateReceived(Update update) {
        router.route(update, this);
    }
}
