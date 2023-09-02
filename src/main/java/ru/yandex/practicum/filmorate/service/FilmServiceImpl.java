package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genres;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.FilmDbStorage;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FilmServiceImpl implements FilmService {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final FilmDbStorage filmDbStorage;

    @Override
    public List<Film> getFilms() {
        List<Film> filmList = filmDbStorage.getFilms();
        Collections.sort(filmList, Film::compareByDi);
       return filmList;
    }

    @Override
    public Film postFilm(Film film) {
        return filmDbStorage.postFilm(film);
    }

    @Override
    public Film putFilm(Film film) {
        return filmDbStorage.putFilm(film);
    }

    @Override
    public void addLikeFilm(int id, int userId) {
        filmDbStorage.addLikeFilm(id, userId);
    }

    @Override
    public void deleteLikeFilm(int id, int userId) {
        filmDbStorage.deleteLikeFilm(id, userId);
    }

    @Override
    public Film getFilm(Integer id) {
        return filmDbStorage.getFilm(id);
    }

    @Override
    public List<Film> getListBestMovies(Integer count) {

        if (count == null) {
            return getListBestTenMovies();
        }
        log.info("count " + count);
        return filmDbStorage.getFilms().stream().sorted().limit(count).collect(Collectors.toList());
    }

    @Override
    public List<Film> getListBestTenMovies() {
        return  filmDbStorage.getFilms().stream().sorted().limit(10).collect(Collectors.toList());
    }

    @Override
    public MPA getMPA(Integer id) {
        return filmDbStorage.getMPA(id);
    }

    @Override
    public List<MPA> getMPA() {
        List<MPA> list = filmDbStorage.getMPA();
        Collections.sort(list);
        return list;
    }

    @Override
    public Genres getGanres(Integer id) {
        return filmDbStorage.getGanres(id);
    }

    @Override
    public List<Genres> getGanres() {
        return filmDbStorage.getGanres();
    }
}
