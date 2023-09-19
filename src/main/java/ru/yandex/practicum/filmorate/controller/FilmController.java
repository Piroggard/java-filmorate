package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
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

    @DeleteMapping("/films/{filmId}")
    public void deleteFilm(@PathVariable int filmId) {
        filmService.deleteFilm(filmId);
    }

    @GetMapping("/films/popular")
    @ResponseBody
    public List<Film> getPopularFilmsByGenreAndYear(@RequestParam(required = false) Integer count,
                                                    @RequestParam(required = false) Integer genreId,
                                                    @RequestParam(required = false) Integer year) {
        if (count != null) {
            return filmService.getListBestMovies(count);
        }
        if (count == null && genreId == null && year == null) {
            return filmService.getListBestTenMovies();
        }
        return filmService.getPopularFilmsByGenreAndYear(count, genreId, year);
    }
}
