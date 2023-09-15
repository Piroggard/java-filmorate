package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserService {

    List<User> getUsers();

    User postUser(User user);

    User putUser(User user);

    void addFriends(int userId, int friendId);

    void deleteFriends(int userId, int friendId);

    List<User> getUserFriend(int userId);

    List<User> getListMutualFriend(int userId, int otherId);

    User getUser(int id);

    public List<Film> getRecommendation(int userId);
}
