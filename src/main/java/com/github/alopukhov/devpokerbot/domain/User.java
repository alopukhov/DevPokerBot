package com.github.alopukhov.devpokerbot.domain;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import java.time.Instant;

import static lombok.AccessLevel.NONE;
import static lombok.AccessLevel.PACKAGE;

@Table(name = "users")
@Entity
@Data
@NoArgsConstructor(access = PACKAGE)
public class User {
    @Id
    private int id;
    private String nickname;
    @Column(name = "reg_time", updatable = false)
    @Setter(NONE)
    private Instant registeredAt;

    public User(int id, Instant registeredAt) {
        this.id = id;
        this.registeredAt = registeredAt;
    }
}
