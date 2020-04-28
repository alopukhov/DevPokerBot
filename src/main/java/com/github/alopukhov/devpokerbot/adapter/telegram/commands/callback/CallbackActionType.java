package com.github.alopukhov.devpokerbot.adapter.telegram.commands.callback;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum CallbackActionType {
    VOTE(TypeCodes.VOTE_TYPE_CODE),
    REVEAL(TypeCodes.REVEAL_TYPE_CODE);
    @Getter(onMethod_ = @JsonValue)
    private final String typeCode;

    public static class TypeCodes {
        public static final String VOTE_TYPE_CODE = "v";
        public static final String REVEAL_TYPE_CODE = "r";
    }
}
