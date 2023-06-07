package ru.yandex.practicum.filmorate.validation;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

public class ValidationFilm {


    public void validation(Film film) throws ValidationException {
        if (film.getName().length() == 0) {
            throw new ValidationException("Movie title must be filled");
        } else if (film.getDescription().length() > 200) {
            throw new ValidationException("The description of the movie is too long");
        } else if (film.getDuration() <= 0) {
            throw new ValidationException("The film cannot be less than 1 minute long.");
        }
        LocalDate earlyReleaseDate = LocalDate.of(1895, 12, 28);
        if (film.getReleaseDate().isBefore(earlyReleaseDate)) {
            throw new ValidationException("The film cannot be earlier than December 28, 1895");
        }
    }


}
