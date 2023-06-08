package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
public class FilmServiceImpl implements FilmService{
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final InMemoryFilmStorage filmStorage;

    @Override
    public List<Film> getFilms() {
        return filmStorage.getFilms();
    }

    @Override
    public Film postFilm(Film film) {
        return filmStorage.postFilm(film);
    }

    @Override
    public Film putFilm(Film film) {
        return filmStorage.putFilm(film);
    }

    @Override
    public void addLikeFilm(int id, int userId) {
        Film film = filmStorage.films.get(id);
        film.addLike(userId);
        filmStorage.putFilm(film);
    }

    @Override
    public void deleteLikeFilm(int id, int userId) {
        Film film = filmStorage.films.get(id);
        film.deleteLike(userId);
        filmStorage.putFilm(film);
    }

    @Override
    public Film[] getListBestMovies(Integer count) {
        if (count == null){
           return getListBestTenMovies();
        }

            log.info("count " + count);
            List<Film> films = new ArrayList<>();
            for (Integer idFilm : filmStorage.films.keySet()){
                System.out.println(idFilm);
                films.add(filmStorage.films.get(idFilm));
            }
            Collections.sort(films);
            Film [] topFilms = new Film[count];
            for (int i = 0; i < count; i++) {
                topFilms[i] = films.get(i);
            }
            return topFilms;

    }
    @Override
    public Film[] getListBestTenMovies(){
        List<Film> films = new ArrayList<>();
        for (Integer idFilm : filmStorage.films.keySet()){
            films.add(filmStorage.films.get(idFilm));
        }
        Collections.sort(films);
        Film [] topFilms = new Film[10];
        for (int i = 0; i < topFilms.length ; i++) {
            topFilms[i] = films.get(i);
            System.out.println("---   " + i);
        }
        return topFilms;
    }
}
