package ru.yandex.practicum.filmorate.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validation.ValidationUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    ValidationUser validationUser = new ValidationUser();
    private int id = 1;
    public Map<Integer, User> users = new HashMap<>();

    @Override
    public List<User> getUsers() {
        List list = new ArrayList(users.values());
        log.info("Return Users" + list);
        return list;
    }

    @Override
    public User postUser(User user) {
        validationUser.validation(user);
        user.setId(id);
        users.put(id, user);
        id++;
        log.info("User creation" + user);
        return user;
    }

    @Override
    public User putUser(User user) {
        validationUser.validation(user);
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            log.info("Updated user" + user);
            log.info("Users" + users);
        } else {
            throw new NullPointerException("Id unknown");
        }
        return user;
    }
}
