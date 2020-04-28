package com.github.alopukhov.devpokerbot.domain;

import lombok.*;

import javax.persistence.*;

import java.time.Instant;

import static javax.persistence.FetchType.EAGER;
import static lombok.AccessLevel.PACKAGE;
import static lombok.AccessLevel.PRIVATE;

@Entity
@Table(name = "votes")
@Getter
@Builder
@NoArgsConstructor(access = PACKAGE)
@AllArgsConstructor(access = PRIVATE)
public class Vote {
    @Id
    @SequenceGenerator(name = "votes_seq", sequenceName = "votes_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "votes_seq")
    private Long id;
    @ManyToOne(fetch = EAGER, optional = false)
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;
    @ManyToOne(fetch = EAGER, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @Column(name = "choice", nullable = false)
    @Setter
    private Integer choiceId;
    @Setter
    private Instant votedAt;

    public Choice getChoice() {
        if (choiceId == null || game == null) {
            return null;
        }
        return game.getChoice(choiceId);
    }
}
