package com.github.alopukhov.devpokerbot.adapter.telegram.commands.callback;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.alopukhov.devpokerbot.adapter.telegram.TelegramCommandHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.transaction.Transactional;

import static com.github.alopukhov.devpokerbot.adapter.telegram.commands.callback.CallbackCommandUtils.parseAction;
import static com.google.common.base.Preconditions.checkArgument;

@RequiredArgsConstructor
@Slf4j
abstract class CallbackCommandHandlerBase<T extends TelegramCallbackAction> implements CallbackCommandHandler {
    private final ObjectMapper objectMapper;

    @Transactional
    @SuppressWarnings("unchecked")
    @Override
    public void handle(Update update, AbsSender callback) {
        TelegramCallbackAction action = parseAction(update, objectMapper);
        checkArgument(action != null && action.type() == handledType(), "%s can't handle action %s", getClass(), action);
        AnswerCallbackQuery answerCallbackQuery = handle((T) action, update.getCallbackQuery(), callback);
        try {
            callback.execute(answerCallbackQuery);
        } catch (TelegramApiException e) {
            log.error("Can't answer to callbackQuery", e);
        }
    }

    protected abstract AnswerCallbackQuery handle(T action, CallbackQuery callbackQuery, AbsSender callback);

    protected AnswerCallbackQuery createAnswer(CallbackQuery callbackQuery, String message) {
        return new AnswerCallbackQuery()
                .setText(message)
                .setCallbackQueryId(callbackQuery.getId());
    }
}
