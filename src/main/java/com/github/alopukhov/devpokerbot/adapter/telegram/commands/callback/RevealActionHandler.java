package com.github.alopukhov.devpokerbot.adapter.telegram.commands.callback;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.alopukhov.devpokerbot.adapter.telegram.GamePrinter;
import com.github.alopukhov.devpokerbot.adapter.telegram.TelegramToGameUserService;
import com.github.alopukhov.devpokerbot.domain.Game;
import com.github.alopukhov.devpokerbot.domain.User;
import com.github.alopukhov.devpokerbot.service.GameService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
@Slf4j
public class RevealActionHandler extends CallbackCommandHandlerBase<RevealAction> {
    private final GameService gameService;
    private final GamePrinter gamePrinter;
    private final TelegramToGameUserService telegramToGameUserService;

    public RevealActionHandler(ObjectMapper objectMapper, GameService gameService, GamePrinter gamePrinter, TelegramToGameUserService telegramToGameUserService) {
        super(objectMapper);
        this.gameService = gameService;
        this.gamePrinter = gamePrinter;
        this.telegramToGameUserService = telegramToGameUserService;
    }

    @Override
    protected AnswerCallbackQuery handle(RevealAction action, CallbackQuery callbackQuery, AbsSender callback) {
        Game game = gameService.findGameById(action.getGameId()).orElseThrow(() -> new IllegalArgumentException("No such game"));
        User user = telegramToGameUserService.findGameUser(callbackQuery.getFrom());
        if (!user.equals(game.getOwner())) {
            return createAnswer(callbackQuery, "Only " + game.getOwner().getNickname() + " can reveal votes");
        }
        game = gameService.reveal(game, user);
        EditMessageText editMessage = new EditMessageText()
                .setChatId(callbackQuery.getMessage().getChatId())
                .setMessageId(callbackQuery.getMessage().getMessageId())
                .setText(gamePrinter.printGameStateHtml(game))
                .setReplyMarkup(gamePrinter.makeKeyBoard(game))
                .enableHtml(true);
        try {
            callback.execute(editMessage);
        } catch (TelegramApiException e) {
            log.error("Can't update text after reveal", e);
            throw new RuntimeException("Can't update text after reveal", e);
        }
        return createAnswer(callbackQuery, "Revealed");
    }

    @Override
    public CallbackActionType handledType() {
        return CallbackActionType.REVEAL;
    }
}
