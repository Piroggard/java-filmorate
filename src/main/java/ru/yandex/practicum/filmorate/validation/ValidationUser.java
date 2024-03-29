package ru.yandex.practicum.filmorate.validation;


import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

@Component
public class ValidationUser {
    public void validation(User user) throws ValidationException {
        char[] mail = user.getEmail().toCharArray();
        char[] login = user.getLogin().toCharArray();
        boolean validMail = false;
        boolean validLogin = false;
        LocalDate localDateNow = LocalDate.now();
        for (char c : mail) {
            if (c == '@') {
                validMail = true;
                break;
            }
        }
        for (char c : login) {
            if (c == ' ') {
                validLogin = true;
                break;
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

        if (localDateNow.isBefore(user.getBirthday())) {
            throw new ValidationException("Date of birth cannot be greater than the current date");
        }
    }

    public void validationAddFriend(int id, int idFriend) {
        if (id == idFriend) {
            throw new ValidationException("Unable to add or remove myself from friends list");
        } else if (id < 1 || idFriend  < 1) {
            throw new DataNotFoundException("ID cannot be negative");
        }
    }

    public void searchValidation(User user) {
        if (user == null) {
            throw new DataNotFoundException("Object not found by specified id");
        }
    }

    public void checkId(int id) {
        if (id < 1) {
            throw new DataNotFoundException("ID cannot be negative");
        }
    }
}

