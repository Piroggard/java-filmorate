package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genres;
import ru.yandex.practicum.filmorate.model.MPA;

import java.util.List;

public interface FilmService {

    List<Film> getFilms();

    Film postFilm(Film film);

    Film putFilm(Film film);

    void addLikeFilm(int id, int userId);

    void deleteLikeFilm(int id, int userId);

    Film getFilm(Integer id);

    List<Film> getListBestMovies(Integer count);

    List<Film> getListBestTenMovies();

    MPA getMPA (Integer id);

    List<MPA> getMPA ();

    Genres getGanres (Integer id);

    List<Genres> getGanres ();


}