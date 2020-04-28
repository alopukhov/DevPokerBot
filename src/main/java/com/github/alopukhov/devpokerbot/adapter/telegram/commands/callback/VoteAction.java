package com.github.alopukhov.devpokerbot.adapter.telegram.commands.callback;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.alopukhov.devpokerbot.domain.Choice;
import com.github.alopukhov.devpokerbot.domain.Game;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class VoteAction implements TelegramCallbackAction {
    @JsonProperty("g")
    private long gameId;
    @JsonProperty("c")
    private int choiceId;

    public static VoteAction create(@NonNull Game game, @NonNull Choice choice) {
        VoteAction action = new VoteAction();
        action.gameId = game.getId();
        action.choiceId = choice.getId();
        return action;
    }

    @Override
    public CallbackActionType type() {
        return CallbackActionType.VOTE;
    }
}
