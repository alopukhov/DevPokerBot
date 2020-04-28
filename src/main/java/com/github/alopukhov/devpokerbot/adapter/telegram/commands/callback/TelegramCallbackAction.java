package com.github.alopukhov.devpokerbot.adapter.telegram.commands.callback;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import static com.github.alopukhov.devpokerbot.adapter.telegram.commands.callback.CallbackActionType.TypeCodes.REVEAL_TYPE_CODE;
import static com.github.alopukhov.devpokerbot.adapter.telegram.commands.callback.CallbackActionType.TypeCodes.VOTE_TYPE_CODE;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "a")
@JsonSubTypes({
        @JsonSubTypes.Type(value = VoteAction.class, name = VOTE_TYPE_CODE),
        @JsonSubTypes.Type(value = RevealAction.class, name = REVEAL_TYPE_CODE)
})
public interface TelegramCallbackAction {
    @JsonProperty("a")
    CallbackActionType type();
}
