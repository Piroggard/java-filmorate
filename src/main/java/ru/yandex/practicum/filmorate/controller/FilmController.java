package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validation.ValidationFilm;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController
public class FilmController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    ValidationFilm validationFilm = new ValidationFilm();
    private int id = 1;
    List<Film> films = new CopyOnWriteArrayList<>();

    @GetMapping("/films")
    public List<Film> getFilms() {
        log.info("Receiving a request");
        return films;
    }

    @PostMapping("/films")
    public Film postFilm(@RequestBody Film film) {
        validationFilm.validation(film);
        film.setId(id);
        films.add(film);
        id++;
        log.info("Film making" + film);
        return film;
    }

    @PutMapping("/films")
    public Film putFilm(@RequestBody Film film) {
        validationFilm.validationId(film, films);
        validationFilm.validation(film);
        List<Film> filmsList = new CopyOnWriteArrayList<>(films);
        for (int i = 0; i < filmsList.size(); i++) {
            if (filmsList.get(i).getId() == film.getId()) {
                films.remove(i);
                films.add(i, film);
                log.info("Movie update" + film);
            }
        }
        return film;
    }
}
