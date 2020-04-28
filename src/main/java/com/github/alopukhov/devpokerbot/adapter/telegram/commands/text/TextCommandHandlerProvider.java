package com.github.alopukhov.devpokerbot.adapter.telegram.commands.text;

import com.github.alopukhov.devpokerbot.adapter.telegram.TelegramCommandHandler;
import com.github.alopukhov.devpokerbot.adapter.telegram.commands.CommandHandlerProvider;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.*;

import static com.github.alopukhov.devpokerbot.adapter.telegram.commands.text.TextCommandUtils.getTextCommand;
import static java.util.List.copyOf;
import static java.util.Optional.empty;

@Service
public class TextCommandHandlerProvider implements CommandHandlerProvider {
    private Map<String, List<TextCommandHandler>> handlersMap;
    private List<TextCommandHandler> allTextCommandHandlers;

    public TextCommandHandlerProvider(Collection<TextCommandHandler> textCommandHandlers) {
        allTextCommandHandlers = copyOf(textCommandHandlers);
        handlersMap = new HashMap<>();
        for (TextCommandHandler handler : allTextCommandHandlers) {
            for (String alias : handler.commandAliases()) {
                handlersMap.computeIfAbsent(alias, any -> new ArrayList<>()).add(handler);
            }
        }
    }

    @Override
    public Optional<TelegramCommandHandler> getHandler(Update update) {
        String alias = getTextCommand(update);
        if (alias == null) {
            return empty();
        }
        for (TextCommandHandler handler : handlersMap.getOrDefault(alias, allTextCommandHandlers)) {
            if (handler.supports(update)) {
                return Optional.of(handler);
            }
        }
        return empty();
    }
}
