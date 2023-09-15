package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;
import ru.yandex.practicum.filmorate.validation.ValidationUser;

import java.util.*;

@Component
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private UserDbStorage userDbStorage;
    final FilmService filmService;
    final FilmServiceImpl filmServiceImpl;
    private FilmDbStorage filmDbStorage;
    public final JdbcTemplate jdbcTemplate;
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
        return userDbStorage.getFriendsUser(userId);

    }

    @Override
    public List<User> getListMutualFriend(int userId, int otherId) {
        validationUser.validationAddFriend(userId, otherId);
        return userDbStorage.getListMutualFriend(userId, otherId);
    }


    @Override
    public List<Film> getRecommendation(int userId) {
        // Для каждого юзера создается мапа фильм-оценка.
        Map<User, Map<Film, Double>> data = new HashMap<>();
        List<User> userList = userDbStorage.getUsers();
        // Заполнение данных.
        userList.forEach(u -> data.put(u, userDbStorage.getLikeListByUser(u.getId())));
        // Вычленяем данные обрабатываемого юзера.
        User user = userDbStorage.getUser(userId);
        Map<Film, Double> userData = data.get(user);
        // Убираем его из общей базы.
        data.remove(user);

        // Приступаем к обработке.
        // Мапа, сортированнная по количеству совпадений оценок, в порядке убывания.
        Map<Long, Map<Film, Double>> nearestUsersData = new TreeMap<>(
                Comparator.comparingLong((Long k) -> k).reversed());
        for (Map.Entry<User, Map<Film, Double>> entryData : data.entrySet()) {
            long amountOfHit = 0;
            for (Map.Entry<Film, Double> entryFilmsForCheckedUser : entryData.getValue().entrySet()) {
                // Ищем совпадение оценок.
                // На данном этапе есть только лайки, т.е. в поле Double может быть лишь единица,
                // поэтому просто проверяем наличие.
                if (userData.containsKey(entryFilmsForCheckedUser.getKey()))
                    amountOfHit++;
            }
            // Данные пользователя, у которого есть совпадения, сохраняем. Ключ - кол-во совпадений.
            if (amountOfHit > 0)
                nearestUsersData.put(amountOfHit, entryData.getValue());
        }
        List<Film> result = new ArrayList<>();
        // Ищем фильмы с лайками, которые есть у ближайшего юзера, но не у проверяемого,
        // начиная с фильмов юзера с максимальным совпадением.
        for (Map.Entry<Long, Map<Film, Double>> e : nearestUsersData.entrySet()) {
            for (Map.Entry<Film, Double> entry : e.getValue().entrySet()) {
                if (!userData.containsKey(entry.getKey()))
                    result.add(entry.getKey());
            }
            // Если нашли рекомендации, то выходим. Нет - переходим к списку фильмов следующего юзера.
            if (result.size() > 0) break;
        }
        filmServiceImpl.loadDataIntoFilm(result);
        return result;
    }

}
