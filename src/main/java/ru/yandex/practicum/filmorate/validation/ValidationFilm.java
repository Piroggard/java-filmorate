package ru.yandex.practicum.filmorate.validation;

import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;


public class ValidationFilm {
    final int MAXIMUM_LENGTH_OF_FILM_DESCRIPTION = 200;
    final LocalDate EARLY_RELEASE_DATE = LocalDate.of(1895, 12, 28);

    public void validation(Film film) throws ValidationException {
        if (film.getName().length() == 0) {
            throw new ValidationException("Movie title must be filled");
        } else if (film.getDescription().length() > MAXIMUM_LENGTH_OF_FILM_DESCRIPTION) {
            throw new ValidationException("The description of the movie is too long");
        } else if (film.getDuration() <= 0) {
            throw new ValidationException("The film cannot be less than 1 minute long.");
        }
        if (film.getReleaseDate().isBefore(EARLY_RELEASE_DATE)) {
            throw new ValidationException("The film cannot be earlier than December 28, 1895");
        }
    }

    public void validationIdFilm(int id) {
        if (id < 1) {
            throw new DataNotFoundException("ID cannot be negative");
        }
    }

    public void searchValidation(Film film) {
        if (film == null) {
            throw new DataNotFoundException("Object not found by specified id");
        }
    }
}
