package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestUser {
    public UserController userController = new UserController();

    @Test
    void createAnObject() {
        User user = new User("abc@yandex.ru", "MaxFed", "Max", LocalDate.of(2000, 12,
                25));
        userController.postUser(user);
        assertEquals(1, userController.getUsers().size(), "Object created successfully");
    }

    @Test
    void createAnObjectEmailFieldNotFilled() {
        User user = new User("", "MaxFed", "Max", LocalDate.of(2000, 12,
                25));
        assertThrows(ValidationException.class, () -> userController.postUser(user), "Address not filled");
    }

    @Test
    void createAnObjectEmailNoRequiredSign() {
        User user = new User("abcyandex.ru", "MaxFed", "Max", LocalDate.of(2000, 12,
                25));
        assertThrows(ValidationException.class, () -> userController.postUser(user), "No sign @ in address");
    }

    @Test
    void createAnObjectEmptyFieldLogin() {
        User user = new User("abcyandex@.ru", "", "Max", LocalDate.of(2000, 12,
                25));
        assertThrows(ValidationException.class, () -> userController.postUser(user), "Login not completed");
    }

    @Test
    void createAnObjectSpacebarsInLogin() {
        User user = new User("abcyandex@.ru", "Max Fed", "Max", LocalDate.of(2000, 12,
                25));
        assertThrows(ValidationException.class, () -> userController.postUser(user), "Space in logging");
    }

    @Test
    void createAnObjectinNameWeUseTheLogin() {
        User user = new User("abcyandex@.ru", "MaxFed", "", LocalDate.of(2000, 12,
                25));
        userController.postUser(user);
        assertEquals(user.getName(), user.getLogin(), "write the login in the name");
    }

    @Test
    void createAnObjectdataOfBirthCheck() {
        User user = new User("abcyandex@.ru", "MaxFed", "Max", LocalDate.of(2024, 12,
                25));
        assertThrows(ValidationException.class, () -> userController.postUser(user), "wrong date of birth");
    }
}
