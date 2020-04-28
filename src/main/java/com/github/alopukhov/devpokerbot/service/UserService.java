package com.github.alopukhov.devpokerbot.service;

import com.github.alopukhov.devpokerbot.domain.User;
import com.github.alopukhov.devpokerbot.repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import java.time.Clock;

import static javax.transaction.Transactional.TxType.MANDATORY;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final Clock clock;

    @Transactional(MANDATORY)
    public User getOrCreateUser(int telegramUserId) {
        return userRepository.findOneById(telegramUserId)
                .orElseGet(() -> userRepository.save(new User(telegramUserId, clock.instant())));
    }

    @Transactional(MANDATORY)
    public User setNickName(@NonNull User user, String nickname) {
        user.setNickname(nickname);
        return userRepository.save(user);
    }
}
