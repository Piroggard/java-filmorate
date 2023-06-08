package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmService {
    public List<Film> getFilms();
    public Film postFilm(Film film);
    public Film putFilm(Film film);
    public void addLikeFilm (int id , int userId);
    public void deleteLikeFilm (int id , int userId);
    public List<Film> getFilm(Integer id);

    public Film[] getListBestMovies (Integer count);
    public Film[] getListBestTenMovies();

}
