package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {

   List<Film> getFilms();

   Film postFilm(Film film);

   Film putFilm(Film film);
}
