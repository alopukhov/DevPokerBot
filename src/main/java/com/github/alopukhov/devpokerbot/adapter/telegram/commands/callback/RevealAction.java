package com.github.alopukhov.devpokerbot.adapter.telegram.commands.callback;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.alopukhov.devpokerbot.domain.Game;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RevealAction implements TelegramCallbackAction {
    @JsonProperty("g")
    private long gameId;

    public static RevealAction create(@NonNull Game game){
        return new RevealAction(game.getId());
    }

    @Override
    public CallbackActionType type() {
        return CallbackActionType.REVEAL;
    }
}
