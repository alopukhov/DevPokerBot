package com.github.alopukhov.devpokerbot.repositories;

import com.github.alopukhov.devpokerbot.domain.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, String> {
    Optional<User> findOneById(int id);
}
