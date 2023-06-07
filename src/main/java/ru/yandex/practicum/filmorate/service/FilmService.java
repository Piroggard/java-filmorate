package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmService {
    public List<Film> getFilms();
    public Film postFilm(Film film);
    public Film putFilm(Film film);
}
