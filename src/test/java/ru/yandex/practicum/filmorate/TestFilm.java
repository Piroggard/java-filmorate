package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.*;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestFilm {

    public FilmController filmController = new FilmController();


    @Test
    void createAnObject() {
        Film film = new Film("Naim", "description", LocalDate.of(2000, 12, 25)
                , 2);
        filmController.postFilm(film);
        assertEquals(1, filmController.getFilms().size(), "Verification successful");
    }

    @Test
    void createAnObjectWrongReleaseDate() {
        Film film = new Film("Naim", "description", LocalDate.of(1800, 12, 25)
                , 2);
        assertThrows(ValidationException.class, () -> filmController.putFilm(film), "Wrong release date");

    }

    @Test
    void createAnObjectEmptyName() {
        Film film = new Film("", "description", LocalDate.of(1995, 12, 25)
                , 2);
        assertThrows(ValidationException.class, () -> filmController.putFilm(film), "No name");
    }

    @Test
    void createAnObjectMaximumDescription201() {
        Film film = new Film("Naim", "ааааааааааааааааааааааааааааааааааааааааааааааааааааааааааааааа" +
                "аааааааааааааааааааааааааааааaааааааааааааааааааааааааааааааааааааааааааааааааааааааааааааааааааааааа" +
                "ааааааааааааааааааааааааaааaaaaaaaaaа",
                LocalDate.of(1995, 12, 25)
                , 2);
        assertThrows(ValidationException.class, () -> filmController.putFilm(film), "Description too long");
    }

    @Test
    void createAnObjectMaximumDescription200() {
        Film film = new Film("Naim", "ааааааааааааааааааааааааааааааааааааааааааааааааааааааааааааааа" +
                "аааааааааааааааааааааааааааааaааааааааааааааааааааааааааааааааааааааааааааааааааааааааааааааааааааааа" +
                "ааааааааааааааааааааааааааaaaaaaaaaа",
                LocalDate.of(1995, 12, 25)
                , 2);
        System.out.println(film.getDescription().length());
        filmController.postFilm(film);
        assertEquals(1, filmController.getFilms().size(), "Description 200 characters");
    }

    @Test
    void createAnObjectduration1m() {
        Film film = new Film("Naim", "description",
                LocalDate.of(1995, 12, 25)
                , 1);
        filmController.postFilm(film);
        assertEquals(1, filmController.getFilms().size(), "Duration is positive");
    }

    @Test
    void createAnObjectdurationNullm() {
        Film film = new Film("Naim", "description",
                LocalDate.of(1995, 12, 25)
                , 0);
        assertThrows(ValidationException.class, () -> filmController.putFilm(film), "Duration negative");
    }

    @Test
    void createAnObjectPut() {
        Film film = new Film("Naim", "description", LocalDate.of(2000, 12, 25)
                , 2);
        filmController.postFilm(film);
        Film film1 = new Film(1, "Naim", "description", LocalDate.of(2000, 12, 25)
                , 2);
        filmController.putFilm(film1);
        assertEquals(1, filmController.getFilms().size(), "Verification successful");
    }

}
