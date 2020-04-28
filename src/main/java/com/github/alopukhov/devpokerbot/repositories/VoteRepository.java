package com.github.alopukhov.devpokerbot.repositories;

import com.github.alopukhov.devpokerbot.domain.Game;
import com.github.alopukhov.devpokerbot.domain.User;
import com.github.alopukhov.devpokerbot.domain.Vote;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface VoteRepository extends CrudRepository<Vote, Long> {
    Optional<Vote> findOnyByGameAndUser(Game game, User user);
    List<Vote> findAllVotesByGame(Game game);
}
