package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.service.UserServiceImpl;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
@Component
public class TestUser {
    InMemoryUserStorage userStorage = new InMemoryUserStorage();
    UserService userService = new UserServiceImpl(userStorage);
    UserController userController = new UserController(userService);
    User templateUser;

    @BeforeEach
    public void setData() {
        templateUser = new User("abc@yandex.ru", "MaxFed", "Max", LocalDate.of(2000, 12, 25));
    }

    @Test
    void createAnObject() {
        userController.postUser(templateUser);
        assertEquals(1, userController.getUsers().size(), "Object created successfully");
    }

    @Test
    void createAnObjectEmailFieldNotFilled() {
        templateUser.setEmail("");
        assertThrows(ValidationException.class, () -> userController.postUser(templateUser), "Address not filled");
    }

    @Test
    void createAnObjectEmailNoRequiredSign() {
        templateUser.setEmail("abcyandex.ru");
        assertThrows(ValidationException.class, () -> userController.postUser(templateUser), "No sign @ in address");
    }

    @Test
    void createAnObjectEmptyFieldLogin() {
        templateUser.setLogin("");
        assertThrows(ValidationException.class, () -> userController.postUser(templateUser), "Login not completed");
    }

    @Test
    void createAnObjectSpacebarsInLogin() {
        templateUser.setLogin("Max Fed");
        assertThrows(ValidationException.class, () -> userController.postUser(templateUser), "Space in logging");
    }

    @Test
    void createAnObjectinNameWeUseTheLogin() {
        templateUser.setName("");
        userController.postUser(templateUser);
        assertEquals(templateUser.getName(), templateUser.getLogin(), "write the login in the name");
    }

    @Test
    void createAnObjectdataOfBirthCheck() {
        templateUser.setBirthday(LocalDate.of(2024, 12, 25));
        assertThrows(ValidationException.class, () -> userController.postUser(templateUser), "wrong date of birth");
    }

    @Test
    void createAnObjecPut() {
        userController.postUser(templateUser);
        templateUser.setId(1);
        userController.putUser(templateUser);
        assertEquals(1, userController.getUsers().size(), "Object created successfully");
    }
}
