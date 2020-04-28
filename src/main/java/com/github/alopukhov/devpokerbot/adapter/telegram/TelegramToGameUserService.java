package com.github.alopukhov.devpokerbot.adapter.telegram;

import com.github.alopukhov.devpokerbot.domain.User;
import com.github.alopukhov.devpokerbot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class TelegramToGameUserService {
    private final UserService userService;

    @Transactional
    public User findGameUser(org.telegram.telegrambots.meta.api.objects.User tgUser) {
        User user = userService.getOrCreateUser(tgUser.getId());
        if (user.getNickname() == null) {
            String newNickname;
            if (tgUser.getUserName() != null) {
                newNickname = tgUser.getUserName();
            } else if (tgUser.getFirstName() != null && tgUser.getLastName() != null) {
                newNickname = tgUser.getFirstName() + " " + tgUser.getLastName();
            } else if (tgUser.getLastName() != null) {
                newNickname = tgUser.getLastName();
            } else if (tgUser.getFirstName() != null) {
                newNickname = tgUser.getFirstName();
            } else {
                newNickname = "user" + user.getId();
            }
            user = userService.setNickName(user, newNickname);
        }
        return user;
    }
}
