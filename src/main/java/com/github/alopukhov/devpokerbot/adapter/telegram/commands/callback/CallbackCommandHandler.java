package com.github.alopukhov.devpokerbot.adapter.telegram.commands.callback;

import com.github.alopukhov.devpokerbot.adapter.telegram.TelegramCommandHandler;

public interface CallbackCommandHandler extends TelegramCommandHandler {
    CallbackActionType handledType();
}
