package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.validation.ValidationFilm;

import java.util.List;

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

    @GetMapping("/films/{id}")
    @ResponseBody
    public Film getFilms(@PathVariable Integer id) {
        validationFilm.validationIdFilm(id);
        validationFilm.searchValidation(filmService.getFilm(id));
        return filmService.getFilm(id);
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

    @PutMapping("/films/{id}/like/{userId}")
    public void addLikeFilm(@PathVariable int id, @PathVariable int userId) {
        filmService.addLikeFilm(id, userId);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public void deleteLikeFilm(@PathVariable int id, @PathVariable int userId) {
        validationFilm.validationIdFilm(id);
        validationFilm.validationIdFilm(userId);
        filmService.deleteLikeFilm(id, userId);
    }

    @GetMapping("/films/popular")
    @ResponseBody
    public List<Film> getListBestMovies(@RequestParam(required = false) Integer count) {
        return filmService.getListBestMovies(count);

    }


}
