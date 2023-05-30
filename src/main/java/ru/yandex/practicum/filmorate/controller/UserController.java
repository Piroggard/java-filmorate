package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validation.ValidationUser;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


@RestController
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final ValidationUser validationUser = new ValidationUser();
    private int id = 1;
    List<User> users = new ArrayList<>();

    @GetMapping("/users")
    public List<User> getUsers() {
        log.info("Receiving a request" + users);
        return users;
    }

    @PostMapping("/users")
    public User postUser(@RequestBody User user) {
        validationUser.validation(user);
        user.setId(id);
        users.add(user);
        id++;
        log.info("User creation" + user);
        return user;
    }

    @PutMapping("/users")
    public User putUser(@RequestBody User user) {
        validationUser.validation(user);
        validationUser.validationId(user, users);
        List<User> userList = new CopyOnWriteArrayList<>(users);
        for (int i = 0; i < userList.size(); i++) {
            if (userList.get(i).getId() == user.getId()) {
                users.remove(i);
                users.add(i, user);
                log.info("User data updates" + user);
            }
        }
        return user;
    }
}
