package com.github.alopukhov.devpokerbot.adapter.telegram;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.alopukhov.devpokerbot.adapter.telegram.commands.callback.RevealAction;
import com.github.alopukhov.devpokerbot.adapter.telegram.commands.callback.TelegramCallbackAction;
import com.github.alopukhov.devpokerbot.adapter.telegram.commands.callback.VoteAction;
import com.github.alopukhov.devpokerbot.domain.Game;
import com.github.alopukhov.devpokerbot.domain.User;
import com.github.alopukhov.devpokerbot.domain.Vote;
import com.google.common.escape.Escaper;
import com.google.common.html.HtmlEscapers;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.collect.Lists.partition;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Slf4j
public class GamePrinter {
    private static final String HEADER = "\u2660\uFE0F\u2663\uFE0F\u2666\uFE0F\u2665\uFE0F\u2660\uFE0F\u2663\uFE0F\u2666\uFE0F\u2665\uFE0F\u2660\uFE0F\u2663\uFE0F\u2666\uFE0F\u2665\uFE0F\n";
    private static final int MAX_KEYS_PER_LINE = 5;
    private final Escaper escaper = HtmlEscapers.htmlEscaper();
    private final ObjectMapper objectMapper;

    public String printGameStateHtml(Game game) {
        StringBuilder sb = new StringBuilder(400);
        sb.append(HEADER);
        sb.append("Planning");
        if (game.getDescription() != null) {
            sb.append(' ').append(escaper.escape(game.getDescription()));
        }
        List<Vote> votes = game.getVotes();
        sb.append(votes.isEmpty() ? "\nNo votes." : "\nVotes:");
        votes.forEach(vote -> {
            sb.append('\n');
            mentionUser(vote.getUser(), sb);
            if (game.isRevealed()) {
                sb.append(" : ").append(escaper.escape(vote.getChoice().displayText()));
            }
        });
        return sb.toString();
    }

    public InlineKeyboardMarkup makeKeyBoard(Game game) {
        if (game.isRevealed()) {
            return null;
        }
        List<InlineKeyboardButton> voteButtons = game.getDeck().getChoices().stream()
                .map(choice -> makeButton(choice.displayText(), VoteAction.create(game, choice)))
                .collect(toList());
        if (voteButtons.isEmpty()) {
            return null;
        }

        int choiceLines = (voteButtons.size() + MAX_KEYS_PER_LINE - 1) / MAX_KEYS_PER_LINE;
        int keysPerLine = (voteButtons.size() + choiceLines - 1) / choiceLines;
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>(partition(voteButtons, keysPerLine));
        keyboard.add(singletonList(makeButton("Reveal", RevealAction.create(game))));
        return new InlineKeyboardMarkup(keyboard);
    }

    private void mentionUser(User user, StringBuilder to) {
        to.append("<a href=\"tg://user?id=").append(user.getId()).append("\">")
                .append(escaper.escape(user.getNickname()))
                .append("</a>");
    }

    private InlineKeyboardButton makeButton(String text, TelegramCallbackAction callbackAction) {
        InlineKeyboardButton button = new InlineKeyboardButton(text);
        button.setCallbackData(callbackData(callbackAction));
        return button;
    }

    private String callbackData(@NonNull TelegramCallbackAction action) {
        try {
            return objectMapper.writeValueAsString(action);
        } catch (JsonProcessingException e) {
            log.error("Can't serialize TelegramCallbackAction: {}", action, e);
            throw new RuntimeException("Can't serialize TelegramCallbackAction", e);
        }
    }
}
