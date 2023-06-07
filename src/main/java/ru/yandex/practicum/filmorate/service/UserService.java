package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserService {
    public List<User> getUsers();
    public User postUser(User user);
    public User putUser(User user);
    public void addFriends (int userId , int friendId);
    public void deleteFriends (int userId , int friendId);
    public List<User>  getUserFriend (int userId);
    public List<User>  getListMutualFriend (int userId , int otherId );

}
