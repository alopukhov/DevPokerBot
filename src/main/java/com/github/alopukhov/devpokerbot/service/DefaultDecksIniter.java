package com.github.alopukhov.devpokerbot.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.alopukhov.devpokerbot.domain.Deck;
import com.github.alopukhov.devpokerbot.domain.DeckContent;
import com.github.alopukhov.devpokerbot.repositories.DeckRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Component
@ConditionalOnProperty(value = "setup-default-decks", havingValue = "true")
@RequiredArgsConstructor
@Slf4j
public class DefaultDecksIniter {
    private final DeckRepository deckRepository;
    private final ObjectMapper objectMapper;
    private final DeckService deckService;

    @Transactional
    @PostConstruct
    public void setupDefaultDecks() {
        List<Deck> decks = readDefaultDecks();
        deckRepository.deleteAll();
        deckRepository.saveAll(decks);
        deckService.refreshDecks();
    }

    private List<Deck> readDefaultDecks() {
        DeckContent[] deckContents;
        try (InputStream is = new ClassPathResource("default-decks.json").getInputStream()) {
            deckContents = objectMapper.readValue(is, DeckContent[].class);
        } catch (IOException e) {
            log.error("Can't read decks", e);
            throw new IllegalStateException(e);
        }
        List<Deck> defaultDecks = new ArrayList<>(deckContents.length);
        for (int i = 0; i < deckContents.length; i++) {
            defaultDecks.add(Deck.builder()
                    .id(i)
                    .alias(deckContents[i].getAlias())
                    .content(deckContents[i])
                    .build());
        }
        return defaultDecks;
    }
}
