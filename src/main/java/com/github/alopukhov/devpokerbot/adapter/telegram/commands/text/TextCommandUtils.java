package com.github.alopukhov.devpokerbot.adapter.telegram.commands.text;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
class TextCommandUtils {
    public static final String COMMAND_PREFIX = "/";
    private static final Pattern COMMAND_REGEXP = Pattern.compile("^(\\/\\w+)(?:\\s|$)");

    public static String getTextCommand(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String text = update.getMessage().getText();
            Matcher matcher = COMMAND_REGEXP.matcher(text);
            if (matcher.find()) {
                return matcher.group(1);
            }
        }
        return null;
    }

    static String getTextCommandWithoutPrefix(Update update) {
        String withPrefix = getTextCommand(update);
        return withPrefix == null? null : withPrefix.substring(COMMAND_PREFIX.length());
    }
}
