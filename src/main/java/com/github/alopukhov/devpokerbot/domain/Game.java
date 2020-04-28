package com.github.alopukhov.devpokerbot.domain;

import lombok.*;

import javax.persistence.*;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static java.util.Collections.unmodifiableList;
import static javax.persistence.FetchType.EAGER;
import static javax.persistence.FetchType.LAZY;
import static lombok.AccessLevel.*;

@Entity
@Table(name = "games")
@EqualsAndHashCode
@Getter
@AllArgsConstructor(access = PRIVATE)
@NoArgsConstructor(access = PACKAGE)
@Builder
public class Game {
    @Id
    @SequenceGenerator(name = "games_seq", sequenceName = "games_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "games_seq")
    @Setter(NONE)
    private Long id;
    private String description;
    @ManyToOne(fetch = EAGER, optional = false)
    @JoinColumn(name = "deck_id", nullable = false)
    private Deck deck;
    @ManyToOne(fetch = EAGER, optional = false)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;
    private Instant startedAt;
    @Setter
    private Instant revealedAt;
    @OneToMany(cascade = CascadeType.ALL, fetch = LAZY, mappedBy = "game")
    @Builder.Default
    private List<Vote> votes = new ArrayList<>();

    public boolean isRevealed() {
        return revealedAt != null;
    }

    public Choice getChoice(int choiceId) {
        return deck.getChoice(choiceId);
    }
}
