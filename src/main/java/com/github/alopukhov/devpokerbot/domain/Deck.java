package com.github.alopukhov.devpokerbot.domain;

import com.github.alopukhov.devpokerbot.adapter.database.DeckContentConverter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import java.util.Collections;
import java.util.List;

import static java.util.Collections.unmodifiableList;
import static lombok.AccessLevel.*;

@Entity
@Table(name = "decks")
@Getter
@Builder
@AllArgsConstructor(access = PRIVATE)
@NoArgsConstructor(access = PACKAGE)
public class Deck {
    @Id
    private int id;
    private String alias;
    @Getter(NONE)
    @Convert(converter = DeckContentConverter.class)
    private DeckContent content;

    public boolean hasChoice(int id) {
        return id >= 0 && id < content.getChoices().size();
    }

    public List<Choice> getChoices() {
        return unmodifiableList(content.getChoices());
    }

    public Choice getChoice(int id) {
        if (!hasChoice(id)) {
            return null;
        }
        return content.getChoices().get(id);
    }
}