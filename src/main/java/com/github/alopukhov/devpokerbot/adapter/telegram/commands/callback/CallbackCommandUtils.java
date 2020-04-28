package com.github.alopukhov.devpokerbot.adapter.telegram.commands.callback;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.Update;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class CallbackCommandUtils {
    public static TelegramCallbackAction parseAction(Update update, ObjectMapper objectMapper) {
        if (!update.hasCallbackQuery()) {
            return null;
        }
        String json = update.getCallbackQuery().getData();
        try {
            return objectMapper.readValue(json, TelegramCallbackAction.class);
        } catch (JsonProcessingException e) {
            log.error("Cant parse callback json {}", json, e);
            return null;
        }
    }
}
