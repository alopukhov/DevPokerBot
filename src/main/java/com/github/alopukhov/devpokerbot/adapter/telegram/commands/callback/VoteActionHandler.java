package com.github.alopukhov.devpokerbot.adapter.telegram.commands.callback;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.alopukhov.devpokerbot.adapter.telegram.GamePrinter;
import com.github.alopukhov.devpokerbot.adapter.telegram.TelegramToGameUserService;
import com.github.alopukhov.devpokerbot.domain.Game;
import com.github.alopukhov.devpokerbot.domain.User;
import com.github.alopukhov.devpokerbot.domain.Vote;
import com.github.alopukhov.devpokerbot.domain.VoteResult;
import com.github.alopukhov.devpokerbot.service.GameService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.transaction.Transactional;

@Service
@Slf4j
public class VoteActionHandler extends CallbackCommandHandlerBase<VoteAction> {
    private final GameService gameService;
    private final GamePrinter gamePrinter;
    private final TelegramToGameUserService telegramToGameUserService;

    public VoteActionHandler(ObjectMapper objectMapper, GameService gameService, GamePrinter gamePrinter, TelegramToGameUserService telegramToGameUserService) {
        super(objectMapper);
        this.gameService = gameService;
        this.gamePrinter = gamePrinter;
        this.telegramToGameUserService = telegramToGameUserService;
    }

    @Override
    public CallbackActionType handledType() {
        return CallbackActionType.VOTE;
    }

    @Override
    protected AnswerCallbackQuery handle(VoteAction action, CallbackQuery callbackQuery, AbsSender callback) {
        Game game = gameService.findGameById(action.getGameId())
                .orElseThrow(() -> new IllegalArgumentException("Can't find game for callback"));
        if (game.isRevealed()) {
            return handleRevealedGame(game, callbackQuery, callback);
        }
        User user = telegramToGameUserService.findGameUser(callbackQuery.getFrom());
        VoteResult voteResult = gameService.vote(game, user, action.getChoiceId());
        switch (voteResult.getResult()) {
            case ADDED:
                return handleVoteAdded(game, voteResult.getVote(), callbackQuery, callback);
            case UPDATED:
                return handleVoteUpdated(game, voteResult.getVote(), callbackQuery, callback);
            case FAILED:
                return handleVoteFailed(game, voteResult.getVote(), callbackQuery, callback);
            default:
                throw new IllegalStateException("Unknown result type");
        }
    }

    private AnswerCallbackQuery handleVoteAdded(Game game, Vote vote, CallbackQuery callbackQuery, AbsSender callback) {
        EditMessageText editMessage = new EditMessageText()
                .setChatId(callbackQuery.getMessage().getChatId())
                .setMessageId(callbackQuery.getMessage().getMessageId())
                .setText(gamePrinter.printGameStateHtml(game))
                .setReplyMarkup(callbackQuery.getMessage().getReplyMarkup())
                .enableHtml(true);
        try {
            callback.execute(editMessage);
        } catch (TelegramApiException e) {
            log.error("Can't update text after vote", e);
            throw new RuntimeException("Can't update text after vote", e);
        }
        return createAnswer(callbackQuery, "Vote added");
    }

    private AnswerCallbackQuery handleVoteUpdated(Game game, Vote vote, CallbackQuery callbackQuery, AbsSender callback) {
        return createAnswer(callbackQuery, "Vote updated");
    }

    private AnswerCallbackQuery handleVoteFailed(Game game, Vote vote, CallbackQuery callbackQuery, AbsSender callback) {
        return createAnswer(callbackQuery, "Something went wrong");
    }

    private AnswerCallbackQuery handleRevealedGame(Game game, CallbackQuery callbackQuery, AbsSender callback) {
        return createAnswer(callbackQuery, "Game already revealed");
    }
}
