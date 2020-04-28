package com.github.alopukhov.devpokerbot.repositories;

import com.github.alopukhov.devpokerbot.domain.Game;
import org.springframework.data.repository.CrudRepository;

public interface GameRepository extends CrudRepository<Game, Long> {
}
