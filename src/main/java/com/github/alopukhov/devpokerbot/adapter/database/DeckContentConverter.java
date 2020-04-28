package com.github.alopukhov.devpokerbot.adapter.database;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.alopukhov.devpokerbot.domain.DeckContent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.AttributeConverter;

@RequiredArgsConstructor
@Slf4j
public class DeckContentConverter implements AttributeConverter<DeckContent,byte[]> {
    private final ObjectMapper deckObjectMapper;

    @Override
    public byte[] convertToDatabaseColumn(DeckContent attribute) {
        if (attribute == null) {
            return null;
        }
        try {
            return deckObjectMapper.writeValueAsBytes(attribute);
        } catch (Exception e) {
            log.error("Can't convert deck to json", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public DeckContent convertToEntityAttribute(byte[] dbData) {
        if (dbData == null) {
            return null;
        }
        try {
            return deckObjectMapper.readValue(dbData, DeckContent.class);
        } catch (Exception e) {
            log.error("Can't convert bytes to Deck", e);
            throw new RuntimeException("Can't convert json bytes to deck", e);
        }
    }
}
