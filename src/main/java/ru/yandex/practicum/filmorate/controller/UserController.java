package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.validation.ValidationFilm;
import ru.yandex.practicum.filmorate.validation.ValidationUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@AllArgsConstructor
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    private final ValidationUser validationUser;
    @GetMapping("/users")
    public List<User> getUsers() {
       return userService.getUsers();
    }

    @PostMapping("/users")
    public User postUser(@RequestBody User user) {
        validationUser.validation(user);
        return userService.postUser(user);
    }


    @PutMapping("/users")
    public User putUser(@RequestBody User user) {
        validationUser.validation(user);
        return userService.putUser(user);
    }

    @PutMapping ("/users/{id}/friends/{friendId}")
    public void addFriend (@PathVariable int id , @PathVariable int friendId) {
        validationUser.validationAddFriend(id , friendId);
        userService.addFriends(id , friendId);
    }

    @DeleteMapping("/users/{id}/friends/{friendId}")
    public void deleteFriend (@PathVariable int id , @PathVariable int friendId) {
        validationUser.validationAddFriend(id , friendId);
        userService.deleteFriends(id , friendId);
    }

    @GetMapping("/users/{id}/friends")
    public List<User> getUserFriend(@PathVariable int id) {
        return userService.getUserFriend(id);
    }

    @GetMapping("/users/{id}/friends/common/{otherId}")
    public List<User> getUserFriend(@PathVariable int id , @PathVariable int otherId) {
        return userService.getListMutualFriend(id , otherId);
    }
}
