package com.github.alopukhov.devpokerbot.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.*;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PRIVATE;

@Getter
@AllArgsConstructor(access = PRIVATE)
public class Choice {
    @JsonInclude(NON_NULL)
    private final Double val;
    @JsonInclude(NON_NULL)
    private final String text;
    @With
    @JsonIgnore
    private Integer id;

    @JsonCreator
    public Choice(@JsonProperty("val") Double val, @JsonProperty("text") String text) {
        this.val = val;
        this.text = text;
    }

    public String displayText() {
        return text != null? text : val.toString();
    }
}
