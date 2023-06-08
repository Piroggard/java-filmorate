package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.validation.ValidationFilm;

import java.lang.reflect.Array;
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
    @PutMapping("/films/{id}/like/{userId}")
    public void addLikeFilm(@PathVariable int id, @PathVariable int userId){
        filmService.addLikeFilm(id , userId);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public void deleteLikeFilm(@PathVariable int id, @PathVariable int userId){
        filmService.deleteLikeFilm(id , userId);
    }
    @GetMapping("/films/popular")
    @ResponseBody
    public Film[] getListBestMovies(@RequestParam (required = false) Integer count){
        System.out.println(count);

            return filmService.getListBestMovies(count);

    }

    /*@GetMapping("/films/popular")
    public Film[] getListBestTenMovies(){
        return filmService.getListBestTenMovies();
    }*/

    @ExceptionHandler
    public Map<String, String> handleNegativeCount(final ValidationException e) {
        return Map.of("error", "Передан отрицательный параметр count.");
    }
}
