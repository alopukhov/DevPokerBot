package com.github.alopukhov.devpokerbot.adapter.telegram.commands.text;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Collection;

import static com.github.alopukhov.devpokerbot.adapter.telegram.commands.text.TextCommandUtils.getTextCommand;
import static com.google.common.base.Strings.nullToEmpty;
import static java.lang.Math.max;
import static java.util.Collections.singletonList;

@Service
@RequiredArgsConstructor
@Slf4j
class PingCommandHandler implements TextCommandHandler {
    private static final String ALIAS = "/ping";
    private final DateTimeFormatter formatter = DateTimeFormatter.ISO_INSTANT;
    private final Clock clock;

    @Override
    public void handle(Update update, AbsSender callback) {
        Message newMsg = update.getMessage();
        int sendTs = newMsg.getDate();
        Instant sendAt = Instant.ofEpochSecond(sendTs);
        Instant now = clock.instant();
        Duration delay = Duration.between(sendAt, now);
        String userName = newMsg.getFrom().getUserName();
        String firstName = newMsg.getFrom().getFirstName();
        String lastName = newMsg.getFrom().getLastName();
        String from;
        if (userName != null) {
            from = "@" + userName;
        } else if (firstName != null || lastName != null) {
            from = nullToEmpty(firstName) + (firstName == null ? "" : " ") + nullToEmpty(lastName);
        } else {
            from = "Unknown";
        }

        String replyMsg = "Ping from " + from + ".\n" +
                "Received at " + formatter.format(now) + "\n" +
                "Delay ~" + max(0, delay.toSeconds()) + " s.";
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(newMsg.getChatId());
        sendMessage.setText(replyMsg);
        try {
            callback.execute(sendMessage);
        } catch (TelegramApiException e) {
            log.info("can't send ping response", e);
        }
    }

    @Override
    public Collection<String> commandAliases() {
        return singletonList(ALIAS);
    }

    @Override
    public boolean supports(Update update) {
        return ALIAS.equals(getTextCommand(update));
    }
}
