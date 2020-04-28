package com.github.alopukhov.devpokerbot.service;

import com.github.alopukhov.devpokerbot.domain.*;
import com.github.alopukhov.devpokerbot.repositories.GameRepository;
import com.github.alopukhov.devpokerbot.repositories.VoteRepository;
import com.google.common.base.Preconditions;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Clock;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;
import static javax.transaction.Transactional.TxType.MANDATORY;

@Service
@RequiredArgsConstructor
public class GameService {
    private final Clock clock;
    private final GameRepository gameRepository;
    private final VoteRepository voteRepository;

    @Transactional(MANDATORY)
    public Optional<Game> findGameById(long id) {
        return gameRepository.findById(id);
    }

    @Transactional(MANDATORY)
    public Game startGame(@NonNull User owner, @NonNull Deck deck, @NonNull String description) {
        Game game = Game.builder()
                .owner(owner)
                .deck(deck)
                .description(description)
                .startedAt(clock.instant())
                .build();
        return gameRepository.save(game);
    }

    @Transactional(MANDATORY)
    public Game reveal(@NonNull Game game, @NonNull User user) {
        if (game.isRevealed()) {
            return game;
        }
        checkArgument(user.equals(game.getOwner()), "Only owner can reveal game");
        game.setRevealedAt(clock.instant());
        return gameRepository.save(game);
    }

    @Transactional(MANDATORY)
    public VoteResult vote(@NonNull Game game, @NonNull User user, int choiceId) {
        boolean canVote = game.getDeck().hasChoice(choiceId) && !game.isRevealed();
        Optional<Vote> oldVote = voteRepository.findOnyByGameAndUser(game, user);
        if (!canVote) {
            return VoteResult.failed(oldVote.orElse(null));
        }
        Vote vote = oldVote.orElseGet(() -> {
            Vote newVote = Vote.builder().user(user).game(game).build();
            game.getVotes().add(newVote);
            return newVote;
        });
        vote.setChoiceId(choiceId);
        vote.setVotedAt(clock.instant());
        Vote newVote = voteRepository.save(vote);
        return oldVote.isPresent() ? VoteResult.updated(newVote) : VoteResult.added(newVote);
    }

    @Transactional(MANDATORY)
    public List<Vote> findAllVotes(@NonNull Game game) {
        return voteRepository.findAllVotesByGame(game);
    }
}
