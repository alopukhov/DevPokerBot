package com.github.alopukhov.devpokerbot.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import static com.github.alopukhov.devpokerbot.domain.VoteResult.Result.*;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class VoteResult {
    private final Result result;
    private final Vote vote;

    public static VoteResult failed(Vote oldVote) {
        return new VoteResult(FAILED, oldVote);
    }

    public static VoteResult added(@NonNull Vote newVote) {
        return new VoteResult(ADDED, newVote);
    }

    public static VoteResult updated(@NonNull Vote newVote) {
        return new VoteResult(UPDATED, newVote);
    }

    public enum Result {
        ADDED, UPDATED, FAILED
    }
}
