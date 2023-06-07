package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestFilm {
    public FilmController filmController = new FilmController();
    Film templateFilm;
    String description = "a";

    @BeforeEach
    public void setData() {
        templateFilm = new Film("Naim", "description", LocalDate.of(2000, 12, 25), 2);
    }

    @Test
    void createAnObject() {
        filmController.postFilm(templateFilm);
        assertEquals(1, filmController.getFilms().size(), "Verification successful");
    }

    @Test
    void createAnObjectWrongReleaseDate() {
        templateFilm.setReleaseDate(LocalDate.of(1800, 12, 25));
        assertThrows(ValidationException.class, () -> filmController.putFilm(templateFilm), "Wrong release date");
    }

    @Test
    void createAnObjectEmptyName() {
        templateFilm.setName("");
        assertThrows(ValidationException.class, () -> filmController.postFilm(templateFilm), "No name");
    }

    @Test
    void createAnObjectMaximumDescription201() {
        templateFilm.setDescription(description.repeat(201));
        assertThrows(ValidationException.class, () -> filmController.postFilm(templateFilm), "Description too long");
    }

    @Test
    void createAnObjectMaximumDescription200() {
        templateFilm.setDescription(description.repeat(200));
        filmController.postFilm(templateFilm);
        assertEquals(1, filmController.getFilms().size(), "Description 200 characters");
    }

    @Test
    void createAnObjectdurationNullm() {
        templateFilm.setDuration(0);
        assertThrows(ValidationException.class, () -> filmController.putFilm(templateFilm), "Duration negative");
    }

    @Test
    void createAnObjectPut() {
        filmController.postFilm(templateFilm);
        templateFilm.setId(1);
        templateFilm.setName("New film");
        filmController.putFilm(templateFilm);
        assertEquals(1, filmController.getFilms().size(), "Verification successful");
    }
}
