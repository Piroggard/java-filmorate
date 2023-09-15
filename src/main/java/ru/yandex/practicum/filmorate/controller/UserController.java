package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;


@RestController
@AllArgsConstructor
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;


    @GetMapping("/users")
    public List<User> getUsers() {
        log.info("Getting Users");
        return userService.getUsers();
    }

    @GetMapping("/users/{id}")
    public User getUser(@PathVariable int id) {
        log.info("Getting User " + id);
        return userService.getUser(id);
    }

    @PostMapping("/users")
    public User postUser(@RequestBody User user) {
        log.info("Getting User " + user);
        return userService.postUser(user);
    }

    @PutMapping("/users")
    public User putUser(@RequestBody User user) {
        return userService.putUser(user);
    }

    @PutMapping("/users/{id}/friends/{friendId}")
    public void addFriend(@PathVariable int id, @PathVariable int friendId) {
        userService.addFriends(id, friendId);
    }

    @DeleteMapping("/users/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable int id, @PathVariable int friendId) {
        userService.deleteFriends(id, friendId);
    }

    @GetMapping("/users/{id}/friends")
    public List<User> getUserFriend(@PathVariable int id) {
        return userService.getUserFriend(id);
    }

    @GetMapping("/users/{id}/friends/common/{otherId}")
    public List<User> getUserFriend(@PathVariable int id, @PathVariable int otherId) {
        return userService.getListMutualFriend(id, otherId);
    }


    @GetMapping("/users/{id}/recommendations")
    public List<Film> getRecommendation(@PathVariable int id) {
        return userService.getRecommendation(id);
    }

}
