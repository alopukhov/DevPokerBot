package com.github.alopukhov.devpokerbot.adapter.telegram.commands.text;

import com.github.alopukhov.devpokerbot.adapter.telegram.TelegramCommandHandler;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Collection;

public interface TextCommandHandler extends TelegramCommandHandler {
    Collection<String> commandAliases();
    boolean supports(Update update);
}
