package com.github.alopukhov.devpokerbot.adapter.telegram.commands.text;

import com.github.alopukhov.devpokerbot.adapter.telegram.TelegramToGameUserService;
import com.github.alopukhov.devpokerbot.domain.User;
import com.github.alopukhov.devpokerbot.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.transaction.Transactional;
import java.util.Collection;

import static com.github.alopukhov.devpokerbot.adapter.telegram.commands.text.TextCommandUtils.COMMAND_PREFIX;
import static com.github.alopukhov.devpokerbot.adapter.telegram.commands.text.TextCommandUtils.getTextCommand;
import static java.util.Collections.singleton;

@Service
@RequiredArgsConstructor
@Slf4j
class NicknameCommandHandler implements TextCommandHandler {
    public static String ALIAS = COMMAND_PREFIX + "nickname";
    private final TelegramToGameUserService telegramToGameUserService;
    private final UserService userService;

    @Override
    public Collection<String> commandAliases() {
        return singleton(ALIAS);
    }

    @Override
    public boolean supports(Update update) {
        return ALIAS.equals(getTextCommand(update));
    }

    @Transactional
    @Override
    public void handle(Update update, AbsSender callback) {
        User user = telegramToGameUserService.findGameUser(update.getMessage().getFrom());
        String newNick = update.getMessage().getText().substring(ALIAS.length()).trim();
        String msg;
        if (!newNick.isEmpty()) {
            user = userService.setNickName(user, newNick);
            msg = "Your nickname set to " + user.getNickname();
        } else {
            msg = "Your nickname is " + user.getNickname();
        }
        try {
            callback.execute(new SendMessage(update.getMessage().getChatId(), msg));
        } catch (TelegramApiException e) {
            log.error("Can't send answer to set nickname command", e);
        }
    }
}