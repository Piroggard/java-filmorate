package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FilmServiceImpl implements FilmService {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final InMemoryFilmStorage filmStorage;
    private final FilmDbStorage filmDbStorage;

    @Override
    public List<Film> getFilms() {
       /* return filmStorage.getFilms();*/
        return filmDbStorage.getFilms();
    }

    @Override
    public Film postFilm(Film film) {

        //return filmStorage.postFilm(film);
        return filmDbStorage.postFilm(film);
    }

    @Override
    public Film putFilm(Film film) {
        //return filmStorage.putFilm(film);
        return filmDbStorage.putFilm(film);
    }

    @Override
    public void addLikeFilm(int id, int userId) {
        /*Film film = filmStorage.films.get(id);
        film.addLike(userId);
        filmStorage.putFilm(film);*/
        filmDbStorage.addLikeFilm(id, userId);
    }

    @Override
    public void deleteLikeFilm(int id, int userId) {
       /* Film film = filmStorage.films.get(id);
        film.deleteLike(userId);
        filmStorage.putFilm(film);*/
        filmDbStorage.deleteLikeFilm(id, userId);
    }

    @Override
    public Film getFilm(Integer id) {
       /* return filmStorage.films.get(id);*/
        return filmDbStorage.getFilm(id);
    }

    @Override
    public List<Film> getListBestMovies(Integer count) {

        if (count == null) {
            return getListBestTenMovies();
        }
        log.info("count " + count);
        //return filmStorage.films.values().stream().sorted().limit(count).collect(Collectors.toList());
        return filmDbStorage.getFilms().stream().sorted().limit(count).collect(Collectors.toList());
    }

    @Override
    public List<Film> getListBestTenMovies() {
        return  filmDbStorage.getFilms().stream().sorted().limit(10).collect(Collectors.toList());
    }
}
