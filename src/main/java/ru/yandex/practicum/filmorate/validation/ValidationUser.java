package ru.yandex.practicum.filmorate.validation;

import org.springframework.cglib.core.Local;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

public class ValidationUser {
    public void validation(User user) throws ValidationException {
        char[] mail = user.getEmail().toCharArray();
        char[] login = user.getLogin().toCharArray();
        boolean validMail = false;
        boolean validLogin = false;
        for (int i = 0; i < mail.length; i++) {
            if (mail[i] == '@') {
                validMail = true;
            }
        }
        for (int i = 0; i < login.length; i++) {
            if (login[i] == ' ') {
                validLogin = true;
            }
        }
        if (!validMail | user.getEmail().length() == 0) {
            throw new ValidationException("Mail entered incorrectly");
        } else if (validLogin) {
            throw new ValidationException("Login entered incorrectly");
        } else if (user.getLogin().length() == 0) {
            throw new ValidationException("Login cannot be empty");
        }
        if (user.getName() == null) {
            user.setName(user.getLogin());
        } else if (user.getName().length() == 0) {
            user.setName(user.getLogin());
        }
        LocalDate localDateNow = LocalDate.now();

        if (localDateNow.isBefore(user.getBirthday())) {
            throw new ValidationException("Date of birth cannot be greater than the current date");
        }
    }

    public void validationId(User user, List<User> users) {
        boolean filmId = false;
        for (User user1 : users) {
            if (user1.getId() == user.getId()) {
                filmId = true;
            }
        }
        if (!filmId) {
            throw new ValidationException("Id unknown");
        }
    }
}

