package com.github.alopukhov.devpokerbot.adapter.telegram.commands.callback;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.alopukhov.devpokerbot.adapter.telegram.TelegramCommandHandler;
import com.github.alopukhov.devpokerbot.adapter.telegram.commands.CommandHandlerProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.github.alopukhov.devpokerbot.adapter.telegram.commands.callback.CallbackCommandUtils.parseAction;
import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

@Service
@Slf4j
public class CallbackCommandHandlerProvider implements CommandHandlerProvider {
    private final ObjectMapper objectMapper;
    private final Map<CallbackActionType, CallbackCommandHandler> handlers;

    public CallbackCommandHandlerProvider(ObjectMapper objectMapper, List<CallbackCommandHandler> handlers) {
        this.objectMapper = objectMapper;
        this.handlers = handlers.stream().collect(toMap(CallbackCommandHandler::handledType, identity()));
    }

    @Override
    public Optional<TelegramCommandHandler> getHandler(Update update) {
        TelegramCallbackAction action = parseAction(update, objectMapper);
        return action == null ? empty() : ofNullable(handlers.get(action.type()));
    }
}