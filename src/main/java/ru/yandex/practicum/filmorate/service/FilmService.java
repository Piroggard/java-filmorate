package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genres;
import ru.yandex.practicum.filmorate.model.Mpa;

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

    Mpa getMPA(Integer id);

    List<Mpa> getMPA();

    Genres getGanres(Integer id);

    List<Genres> getGanres();

    List<Film> getCommonFilms(int userId, int friendId);

    public List<Film> getRecommendations(Integer id);

    List<Director> getDirectors();

    Director getDirectorsById(Integer id);

    Director postDirectors(Director director);

    Director putDirectors(Director director);

    List<Film> getFilmDirectorYearOrLike(Integer directorId, List<String> sortBy);


    void deleteDirectors(int id);

    List<Film> getFilmPieceNameOrDirectorPieceName(String query, List<String> by);


}