package com.github.alopukhov.devpokerbot.adapter.telegram;

import com.github.alopukhov.devpokerbot.adapter.telegram.commands.CommandHandlerProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TelegramUpdateRouter {
    private final TelegramCommandHandler EMPTY_HANDLER = (anyUpdate, anyCallback) -> { };
    private final List<CommandHandlerProvider> handlerProviders;

    public void route(Update update, AbsSender callback) {
        selectHandler(update).handle(update, callback);
    }

    public TelegramCommandHandler selectHandler(Update update) {
        return handlerProviders.stream()
                .flatMap(p -> p.getHandler(update).stream())
                .findFirst()
                .orElse(EMPTY_HANDLER);
    }
}
