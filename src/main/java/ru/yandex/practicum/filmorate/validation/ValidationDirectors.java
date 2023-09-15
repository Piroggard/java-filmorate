package ru.yandex.practicum.filmorate.validation;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Director;

public class ValidationDirectors {
    public void validation(Director director) {
        if (director.getName().length() == 0 || director.getName().equals(" ")) {
            throw new ValidationException("No director's name");
        }
    }
}
