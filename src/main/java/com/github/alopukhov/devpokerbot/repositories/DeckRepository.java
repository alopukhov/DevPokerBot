package com.github.alopukhov.devpokerbot.repositories;

import com.github.alopukhov.devpokerbot.domain.Deck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface DeckRepository extends JpaRepository<Deck, Integer> {
}
