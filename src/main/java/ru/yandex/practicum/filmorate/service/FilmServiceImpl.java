package ru.yandex.practicum.filmorate.service;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genres;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmDbStorage;
import ru.yandex.practicum.filmorate.validation.ValidationFilm;
import ru.yandex.practicum.filmorate.validation.ValidationGanres;
import ru.yandex.practicum.filmorate.validation.ValidationMpa;

import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
public class FilmServiceImpl implements FilmService {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final FilmDbStorage filmDbStorage;
    private final ValidationFilm validationFilm = new ValidationFilm();
    private final ValidationGanres validationGanres = new ValidationGanres();
    private final ValidationMpa validationMpa = new ValidationMpa();

    @Override
    public List<Film> getFilms() {
        List<Film> filmList = filmDbStorage.getFilms();
        Collections.sort(filmList, Film::compareById);
       return filmList;
    }

    @Override
    public Film postFilm(Film film) {
        validationFilm.validation(film);
        return filmDbStorage.postFilm(film);
    }

    @Override
    public Film putFilm(Film film) {
        validationFilm.validation(film);
        return filmDbStorage.putFilm(film);
    }

    @Override
    public void addLikeFilm(int id, int userId) {
        filmDbStorage.addLikeFilm(id, userId);
    }

    @Override
    public void deleteLikeFilm(int id, int userId) {
        validationFilm.validationIdFilm(id);
        validationFilm.validationIdFilm(userId);
        filmDbStorage.deleteLikeFilm(id, userId);
    }

    @Override
    public Film getFilm(Integer id) {
        validationFilm.validationIdFilm(id);
        validationFilm.searchValidation(filmDbStorage.getFilm(id));
        return filmDbStorage.getFilm(id);
    }

    @Override
    public List<Film> getListBestMovies(Integer count) {

        if (count == null) {
            return getListBestTenMovies();
        }
        log.info("count " + count);
        //return filmDbStorage.getFilms().stream().sorted().limit(count).collect(Collectors.toList());
        return filmDbStorage.getFilmsPopularQuantity(count);
    }

    @Override
    public List<Film> getListBestTenMovies() {
        //return filmDbStorage.getFilms().stream().sorted().limit(10).collect(Collectors.toList());
        return filmDbStorage.getFilmsPopular();

    }

    @Override
    public Mpa getMPA(Integer id) {
        validationMpa.validationId(filmDbStorage.getMPA(id));
        return filmDbStorage.getMPA(id);
    }

    @Override
    public List<Mpa> getMPA() {
        List<Mpa> list = filmDbStorage.getMPA();
        Collections.sort(list);
        return list;
    }

    @Override
    public Genres getGanres(Integer id) {
        validationGanres.validationId(filmDbStorage.getGanres(id));
        return filmDbStorage.getGanres(id);
    }

    @Override
    public List<Genres> getGanres() {
        return filmDbStorage.getGanres();
    }
}
