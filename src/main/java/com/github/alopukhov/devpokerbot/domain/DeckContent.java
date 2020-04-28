package com.github.alopukhov.devpokerbot.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Getter;
import lombok.Singular;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@JsonDeserialize(builder = DeckContent.DeckContentBuilder.class)
public class DeckContent {
    private final String alias;
    private final List<Choice> choices;

    @JsonPOJOBuilder(withPrefix = "")
    public static class DeckContentBuilder {
        public DeckContentBuilder choices(List<Choice> choices) {
            if (choices == null) {
                this.choices = null;
            } else {
                List<Choice> newChoices = new ArrayList<>(choices.size());
                for (int i = 0; i < choices.size(); i++) {
                    newChoices.add(choices.get(i).withId(i));
                }
                this.choices = List.copyOf(newChoices);
            }
            return this;
        }
    }
}
