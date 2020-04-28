package com.github.alopukhov.devpokerbot.service;

import com.github.alopukhov.devpokerbot.domain.Deck;
import com.github.alopukhov.devpokerbot.repositories.DeckRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import static com.google.common.base.Suppliers.memoizeWithExpiration;
import static java.util.Optional.empty;

@Service
public class DeckService {
    private final DeckRepository deckRepository;
    private volatile Supplier<List<Deck>> decks;

    public DeckService(DeckRepository deckRepository) {
        this.deckRepository = deckRepository;
        refreshDecks();
    }

    public Collection<Deck> getDecks() {
        return decks.get();
    }

    public Optional<Deck> findDeckByAlias(String deckAlias) {
        return deckAlias == null ? empty() : getDecks().stream().filter(d -> deckAlias.equals(d.getAlias())).findFirst();
    }

    void refreshDecks() {
        this.decks = memoizeWithExpiration(() -> List.copyOf(deckRepository.findAll()), 5, TimeUnit.MINUTES);
    }
}