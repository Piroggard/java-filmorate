package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.validation.ValidationFilm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
public class FilmController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final ValidationFilm validationFilm = new ValidationFilm();
    private final FilmService filmService;



    @GetMapping("/films")
    public List<Film> getFilms() {
        log.info("Receiving a request");
        return filmService.getFilms();
    }

    @PostMapping("/films")
    public Film postFilm(@RequestBody Film film) {
        validationFilm.validation(film);
        log.info("Film making" + film);
        return filmService.postFilm(film);
    }

    @PutMapping("/films")
    public Film putFilm(@RequestBody Film film) {
        validationFilm.validation(film);

        return filmService.putFilm(film);
    }
}
