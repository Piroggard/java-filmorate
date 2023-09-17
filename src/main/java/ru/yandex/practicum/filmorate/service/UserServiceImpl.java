package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;
import ru.yandex.practicum.filmorate.validation.ValidationUser;

import java.util.List;

@Component
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private UserDbStorage userDbStorage;
    private final ValidationUser validationUser = new ValidationUser();

    @Override
    public List<User> getUsers() {
        return userDbStorage.getUsers();
    }

    @Override
    public User getUser(int id) {
        validationUser.searchValidation(userDbStorage.getUser(id));
        return userDbStorage.getUser(id);
    }

    @Override
    public User postUser(User user) {
        validationUser.validation(user);
        validationUser.searchValidation(user);
        return userDbStorage.postUser(user);
    }

    @Override
    public User putUser(User user) {
        validationUser.validation(user);
        validationUser.searchValidation(user);
        return userDbStorage.putUser(user);
    }

    @Override
    public void addFriends(int userId, int friendId) {
        validationUser.validationAddFriend(userId, friendId);
        log.info("userId {}, friendId{},", userId, friendId);
        userDbStorage.addFriend(userId, friendId);
    }

    @Override
    public void deleteFriends(int userId, int friendId) {
        log.info("userId " + userId + " friendId " + friendId);
        userDbStorage.deleteFriend(userId, friendId);
    }

    @Override
    public List<User> getUserFriend(int userId) {
        validationUser.checkId(userId);
        if(userDbStorage.getUser(userId).equals(null)) {
            throw new DataNotFoundException("Такой пользователь отсутствует.");
        } else return userDbStorage.getFriendsUser(userId);
    }

    @Override
    public List<User> getListMutualFriend(int userId, int otherId) {
        validationUser.validationAddFriend(userId, otherId);
        return userDbStorage.getListMutualFriend(userId, otherId);
    }
    @Override
    public void deleteUser(int userId) {
        validationUser.checkId(userId);
        validationUser.searchValidation(getUser(userId));
        userDbStorage.deleteUser(userId);
    }
}
