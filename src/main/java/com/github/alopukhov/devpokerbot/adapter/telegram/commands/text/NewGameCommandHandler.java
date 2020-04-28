package com.github.alopukhov.devpokerbot.adapter.telegram.commands.text;

import com.github.alopukhov.devpokerbot.adapter.telegram.GamePrinter;
import com.github.alopukhov.devpokerbot.adapter.telegram.TelegramToGameUserService;
import com.github.alopukhov.devpokerbot.domain.Deck;
import com.github.alopukhov.devpokerbot.domain.Game;
import com.github.alopukhov.devpokerbot.domain.User;
import com.github.alopukhov.devpokerbot.service.DeckService;
import com.github.alopukhov.devpokerbot.service.GameService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.bots.AbsSender;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.github.alopukhov.devpokerbot.adapter.telegram.commands.text.TextCommandUtils.COMMAND_PREFIX;
import static com.github.alopukhov.devpokerbot.adapter.telegram.commands.text.TextCommandUtils.getTextCommandWithoutPrefix;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Collections.emptyList;

@Service
@RequiredArgsConstructor
@Slf4j
class NewGameCommandHandler implements TextCommandHandler {
    private final DeckService deckService;
    private final GameService gameService;
    private final TelegramToGameUserService userService;
    private final GamePrinter gamePrinter;

    @Override
    public Collection<String> commandAliases() {
        return emptyList(); //decks are subject to change
    }

    @Override
    public boolean supports(Update update) {
        return deckService.findDeckByAlias(getTextCommandWithoutPrefix(update)).isPresent();
    }

    @Transactional
    @Override
    public void handle(Update update, AbsSender callback) {
        try {
            Game game = createGame(update);
            String markdown = gamePrinter.printGameStateHtml(game);
            InlineKeyboardMarkup keyBoard = gamePrinter.makeKeyBoard(game);
            SendMessage response = new SendMessage(update.getMessage().getChatId(), markdown);
            response.enableHtml(true);
            response.setReplyMarkup(keyBoard);
            callback.execute(response);
        } catch (Exception e) {
            log.error("Can't create new game", e);
            throw new RuntimeException("Can't create new game", e);
        }
    }

    private Game createGame(Update update) {
        String deckAlias = checkNotNull(getTextCommandWithoutPrefix(update));
        User user = userService.findGameUser(update.getMessage().getFrom());
        Deck deck = deckService.findDeckByAlias(deckAlias)
                .orElseThrow(() -> new IllegalStateException("Can't find deck " + deckAlias));
        String description = update.getMessage().getText().substring(deckAlias.length() + COMMAND_PREFIX.length()).trim();
        return gameService.startGame(user, deck, description);
    }
}