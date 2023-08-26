package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;

import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private InMemoryUserStorage inMemoryUserStorage;
    private UserDbStorage userDbStorage;

    @Override
    public List<User> getUsers() {
        return userDbStorage.getUsers();
    }

    @Override
    public User getUser(int id) {
        return userDbStorage.getUser(id);
    }

    @Override
    public User postUser(User user) {
        return userDbStorage.postUser(user);
    }

    @Override
    public User putUser(User user) {
        return userDbStorage.putUser(user);
    }

    @Override
    public void addFriends(int userId, int friendId) {
        log.info("userId " + userId + " friendId " + friendId);
        /*User user = inMemoryUserStorage.users.get(userId);
        User friendsUser = inMemoryUserStorage.users.get(friendId);
        user.addListFriend(friendId);
        friendsUser.addListFriend(userId);
        inMemoryUserStorage.putUser(user);
        inMemoryUserStorage.putUser(friendsUser);*/
        userDbStorage.addFriend(userId, friendId);
    }

    @Override
    public void deleteFriends(int userId, int friendId) {
        log.info("userId " + userId + " friendId " + friendId);
        /*User user = inMemoryUserStorage.users.get(userId);
        User friendsUser = inMemoryUserStorage.users.get(friendId);
        user.deleteFriends(friendId);
        friendsUser.deleteFriends(userId);
        inMemoryUserStorage.putUser(user);
        inMemoryUserStorage.putUser(friendsUser);*/
        userDbStorage.deleteFriend(userId, friendId);
    }

    @Override
    public List<User> getUserFriend(int userId) {
        /*User user = inMemoryUserStorage.users.get(userId);
        List<User> friend = new ArrayList<>();
        for (Integer id : user.getListFriends()) {
            friend.add(inMemoryUserStorage.users.get(id));
        }*/
        return userDbStorage.getFriendsUser(userId);

    }

    @Override
    public List<User> getListMutualFriend(int userId, int otherId) {
        /*User user = inMemoryUserStorage.users.get(userId);
        User user1 = inMemoryUserStorage.users.get(otherId);
        List<Integer> friends = new ArrayList<>(user.getListFriends());
        List<Integer> friends1 = new ArrayList<>(user1.getListFriends());
        List<User> mutualListFriends = new ArrayList<>();
        for (int i = 0; i < friends1.size(); i++) {
            for (int j = 0; j < friends.size(); j++) {
                if (friends1.get(i) == friends.get(j)) {
                    mutualListFriends.add(inMemoryUserStorage.users.get(friends1.get(i)));
                    break;
                }
            }
        }*/
        return userDbStorage.getListMutualFriend(userId, otherId);
    }
}
