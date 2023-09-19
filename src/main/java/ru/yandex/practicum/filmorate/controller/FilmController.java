package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@RestController
@AllArgsConstructor
public class FilmController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final FilmService filmService;

    @GetMapping("/films")
    public List<Film> getFilms() {
        return filmService.getFilms();
    }

    @GetMapping("/films/{id}")
    @ResponseBody
    public Film getFilms(@PathVariable Integer id) {
        return filmService.getFilm(id);
    }

    @PostMapping("/films")
    public Film postFilm(@RequestBody Film film) {
        log.info("Film making" + film);
        return filmService.postFilm(film);
    }

    @PutMapping("/films")
    public Film putFilm(@RequestBody Film film) {
        return filmService.putFilm(film);
    }

    @PutMapping("/films/{id}/like/{userId}")
    public void addLikeFilm(@PathVariable int id, @PathVariable int userId) {
        filmService.addLikeFilm(id, userId);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public void deleteLikeFilm(@PathVariable int id, @PathVariable int userId) {
        filmService.deleteLikeFilm(id, userId);
    }

    @GetMapping("/films/popular")
    @ResponseBody
    public List<Film> getListBestMovies(@RequestParam(required = false) Integer count) {
        return filmService.getListBestMovies(count);
    }

    @GetMapping("/films/director/{directorId}")
    @ResponseBody

    public List<Film> getFilmDirectorYearOrLike(@PathVariable int directorId, @RequestParam (name = "sortBy") List<String> sortBy) {
        return filmService.getFilmDirectorYearOrLike(directorId, sortBy);
    }

    @GetMapping("/films/search")
    @ResponseBody
    public List<Film> getFilmPieceNameOrDirectorPieceName(
        @RequestParam (name = "query") String query,
                @RequestParam (name = "by") List<String> by) {
        return filmService.getFilmPieceNameOrDirectorPieceName(query, by);
    }
}
