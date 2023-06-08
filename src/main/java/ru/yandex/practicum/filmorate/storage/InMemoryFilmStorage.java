package ru.yandex.practicum.filmorate.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validation.ValidationFilm;
import ru.yandex.practicum.filmorate.validation.ValidationUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
public class InMemoryFilmStorage implements FilmStorage{
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    ValidationFilm validationFilm = new ValidationFilm();
    private int id = 1;
    public Map<Integer, Film> films = new HashMap<>();

    @Override
    public List<Film> getFilms() {
        log.info("Receiving a request");
        List<Film> list = new ArrayList<>(films.values());
        return list;
    }

    @Override
    public Film postFilm(Film film) {
        validationFilm.validation(film);
        film.setId(id);
        films.put(id, film);
        id++;
        log.info("Film making" + film);
        return film;
    }

    @Override
    public Film putFilm(Film film) {
        validationFilm.validation(film);
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
        } else {
            throw new ValidationException("Id unknown");
        }
        return film;
    }
}
