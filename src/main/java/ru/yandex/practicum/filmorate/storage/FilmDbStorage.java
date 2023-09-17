package ru.yandex.practicum.filmorate.storage;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genres;
import ru.yandex.practicum.filmorate.model.Mpa;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Component
@AllArgsConstructor
public class FilmDbStorage {
    public final JdbcTemplate jdbcTemplate;


    public List<Film> getFilms() {
        return jdbcTemplate.query("SELECT FILMS_ID AS id, NAME as name , DESCRIPTION as description , RELEASEDATE as releaseDate , DURATION as duration, RATING as rating, GENRE_ID AS genre FROM FILMS f ;", new RowMapper<Film>() {
            @Override
            public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
                Film film = new Film();
                film.setId(rs.getInt("id"));
                film.setName(rs.getString("name"));
                film.setDescription(rs.getString("description"));
                film.setReleaseDate(rs.getDate("releaseDate").toLocalDate());
                film.setDuration(rs.getInt("duration"));
                film.setMpa(getMpa(rs.getInt("rating")));
                film.setGenres(getGanresId(rs.getInt("id")));
                return film;
            }
        });
    }

    public Set<Genres> getGanresId(Integer id) {
        List<Genres> genresList = jdbcTemplate.query("SELECT g.GENRE_ID, g.NAME_GENRE\n" +
                "FROM GENRE g \n" +
                "JOIN FILM_GENRE fg ON fg.GENRE_ID = g.GENRE_ID \n" +
                "JOIN FILMS f ON f.FILMS_ID = fg.FILM_ID \n" +
                "WHERE f.FILMS_ID =?" +
                "ORDER BY g.GENRE_ID ASC;", new RowMapper<Genres>() {
            @Override
            public Genres mapRow(ResultSet rs, int rowNum) throws SQLException {
                Genres genres = new Genres();
                genres.setId(rs.getInt("GENRE_ID"));
                genres.setName(rs.getString("NAME_GENRE"));
                return genres;
            }
        }, id);
        Set<Genres> genres = new HashSet<>();
        for (Genres genres1 : genresList) {
            genres.add(genres1);
        }
        return genres;
    }

    public Film getFilm(Integer idFilm) {
        return jdbcTemplate.queryForObject("select films_id as id, f.name, f.description as description, releasedate as releaseDate, duration , r.reating_id as rating , ul.id_user  as usersLikeMovie, fg.genre_id as genre, " +
                "g.name_genre as nameGenre, r.name as nameMPA, r.description as descriptionMPA " +
                "from films f\n" +
                "              LEFT JOIN reating r on r.reating_id = f.rating\n" +
                "                LEFT JOIN users_like ul on f.films_id = ul.id_films\n" +
                "                LEFT JOIN FILM_GENRE fg  on fg.film_id = f.films_id \n" +
                "                left join genre g on g.genre_id = fg.genre_id where films_id =?;", new RowMapper<Film>() {
            @Override
            public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
                Mpa mpa = new Mpa();
                Set<Integer> usersLikeMovie = new HashSet<>();
                Set<Genres> genres = new HashSet<>();
                Film film = new Film();
                film.setId(rs.getInt("id"));
                film.setName(rs.getString("name"));
                film.setDescription(rs.getString("description"));
                film.setReleaseDate(rs.getDate("releaseDate").toLocalDate());
                film.setDuration(rs.getInt("duration"));
                mpa.setId(rs.getInt("rating"));
                mpa.setName(rs.getString("nameMPA"));
                mpa.setDescription(rs.getString("descriptionMPA"));
                film.setMpa(mpa);
                Integer i = rs.getInt("genre");
                do {
                    usersLikeMovie.add(rs.getInt("usersLikeMovie"));
                    if (i == 0) {
                        break;
                    }
                    Genres genres1 = new Genres();
                    genres1.setId(rs.getInt("genre"));
                    genres1.setName(rs.getString("nameGenre"));
                    genres.add(genres1);
                } while (rs.next());
                List<Genres> genresList = new ArrayList<>(genres);
                Collections.sort(genresList);
                Set<Genres> genresSet = new HashSet<>(genresList);
                film.setGenres(genresSet);
                film.setUsersLikeMovie(usersLikeMovie);
                int like = 0;
                for (Integer integer : usersLikeMovie) {
                    if (integer > 0) {
                        like++;
                    }
                }
                film.setRate(like);
                return film;
            }
        }, idFilm);
    }

    public Film postFilm(Film film) {
        Date date = Date.from(film.getReleaseDate().atStartOfDay(ZoneId.systemDefault()).toInstant());
        java.sql.Date sqlDate = new java.sql.Date(date.getTime());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement("INSERT INTO films (name, description, releasedate, duration, rating) " +
                    "VALUES (?, ?, ?, ?, ?)", new String[]{"films_id"});
            ps.setString(1, film.getName());
            ps.setString(2, film.getDescription());
            ps.setDate(3, sqlDate);
            ps.setInt(4, film.getDuration());
            ps.setInt(5, film.getMpa().getId());
            return ps;
        }, keyHolder);
        Integer keyFilm = keyHolder.getKey().intValue();
        if (film.getGenres() == null) {
            return getFilm(keyFilm);
        }
        List<Genres> genresList = new ArrayList<>();
        for (Genres genres : film.getGenres()) {
            genresList.add(genres);
        }

        jdbcTemplate.batchUpdate("INSERT INTO FILM_GENRE (film_id, genre_id) VALUES(?,?);", new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setInt(1, keyFilm);
                ps.setInt(2, genresList.get(i).getId());
            }

            @Override
            public int getBatchSize() {
                return genresList.size();
            }
        });

        return getFilm(keyFilm);
    }

    public Film putFilm(Film film) {
        jdbcTemplate.update("update films set name = ?, description =?, releasedate = ?, duration  = ?, rating =?" +
                        " where films_id = ?;", film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(),
                film.getMpa().getId(), film.getId());
        if (film.getGenres() == null) {
            jdbcTemplate.update(" DELETE FROM film_genre fg where fg.film_id =?  ;", film.getId());
            return getFilmNotGanre(film.getId());
        }

        Set<Genres> genresSet = film.getGenres();
        if (genresSet.size() == 0) {
            jdbcTemplate.update(" DELETE FROM film_genre fg where fg.film_id =?  ;", film.getId());
            return getFilmNotGanre(film.getId());
        }
        jdbcTemplate.update(" DELETE FROM film_genre fg where fg.film_id =?  ;", film.getId());

        List<Genres> genresList = new ArrayList<>();
        for (Genres genre : film.getGenres()) {
            genresList.add(genre);
        }

        jdbcTemplate.batchUpdate("INSERT INTO film_genre (film_id, genre_id) VALUES(?,?);", new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setInt(1, film.getId());
                ps.setInt(2, genresList.get(i).getId());
            }

            @Override
            public int getBatchSize() {
                return genresList.size();
            }
        });
        return getFilm(film.getId());
    }

    public void addLikeFilm(int id, int userId) {
        jdbcTemplate.update("INSERT INTO users_like (id_user, id_films)\n" +
                "VALUES(?,?);", userId, id);
    }

    public void deleteLikeFilm(int id, int userId) {
        jdbcTemplate.update("delete from users_like where id_user =? and id_films = ?;", userId, id);
    }

    public Mpa getMpa(Integer id) {
        return jdbcTemplate.queryForObject("select reating_id as id , name, description as descriptionMPA from reating r  where reating_id =?;", new RowMapper<Mpa>() {
            @Override
            public Mpa mapRow(ResultSet rs, int rowNum) throws SQLException {
                Mpa mpa = new Mpa();
                mpa.setId(rs.getInt("id"));
                mpa.setName(rs.getString("name"));
                mpa.setDescription(rs.getString("descriptionMPA"));
                return mpa;
            }
        }, id);
    }

    public List<Mpa> getMpa() {
        return jdbcTemplate.query("select reating_id as id, NAME , DESCRIPTION  from reating r;", new RowMapper<Mpa>() {
            @Override
            public Mpa mapRow(ResultSet rs, int rowNum) throws SQLException {
                Mpa mpa = new Mpa();
                mpa.setId(rs.getInt("id"));
                mpa.setName(rs.getString("name"));
                mpa.setDescription(rs.getString("DESCRIPTION"));
                return mpa;
            }
        });
    }

    public Genres getGanres(Integer id) {
        return jdbcTemplate.queryForObject("select genre_id  as id , name_genre as name  from genre g  where genre_id  =?;", new RowMapper<Genres>() {
            @Override
            public Genres mapRow(ResultSet rs, int rowNum) throws SQLException {
                Genres genres = new Genres();
                genres.setId(rs.getInt("id"));
                genres.setName(rs.getString("name"));
                return genres;
            }
        }, id);
    }

    public List<Genres> getGanres() {
        return jdbcTemplate.query("select genre_id  as id , NAME_GENRE AS name  from genre g ;", new RowMapper<Genres>() {
            @Override
            public Genres mapRow(ResultSet rs, int rowNum) throws SQLException {
                Genres genres = new Genres();
                genres.setId(rs.getInt("id"));
                genres.setName(rs.getString("name"));
                return genres;
            }
        });
    }

    public Film getFilmNotGanre(Integer idFilm) {
        return jdbcTemplate.queryForObject("select films_id as id, f.name, f.description as description, releasedate " +
                "as releaseDate, duration , r.reating_id as rating , ul.id_user  as usersLikeMovie,  r.name as nameMPA, r.description " +
                "as descriptionMPA \n" +
                "from films f\n" +
                "              LEFT JOIN reating r on r.reating_id = f.rating\n" +
                "                LEFT JOIN users_like ul on f.films_id = ul.id_films\n" +
                "                LEFT JOIN FILM_GENRE fg  on fg.film_id = f.films_id \n" +
                "                left join genre g on g.genre_id = fg.genre_id  where films_id =?;", new RowMapper<Film>() {
            @Override
            public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
                Mpa mpa = new Mpa();
                Set<Integer> usersLikeMovie = new HashSet<>();
                Set<Genres> genres = new HashSet<>();
                Film film = new Film();
                film.setId(rs.getInt("id"));
                film.setName(rs.getString("name"));
                film.setDescription(rs.getString("description"));
                film.setReleaseDate(rs.getDate("releaseDate").toLocalDate());
                film.setDuration(rs.getInt("duration"));
                mpa.setId(rs.getInt("rating"));
                mpa.setName(rs.getString("nameMPA"));
                mpa.setDescription(rs.getString("descriptionMPA"));
                film.setMpa(mpa);
                film.setGenres(genres);
                do {
                    usersLikeMovie.add(rs.getInt("usersLikeMovie"));
                } while (rs.next());
                film.setUsersLikeMovie(usersLikeMovie);
                int like = 0;
                for (Integer integer : usersLikeMovie) {
                    if (integer > 0) {
                        like++;
                    }
                }
                film.setRate(like);
                return film;
            }
        }, idFilm);
    }

    public List<Film> getFilmsPopularQuantity(int quantityFilm) {
        List<Film> listIdFilm = jdbcTemplate.query("SELECT f.FILMS_ID AS id, f.NAME, f.DESCRIPTION, f.RELEASEDATE, f.DURATION, f.RATING, f.GENRE_ID AS genre, COUNT(ul.ID_FILMS) AS total_likes\n" +
                "FROM FILMS f\n" +
                "LEFT JOIN USERS_LIKE ul ON ul.ID_FILMS = f.FILMS_ID\n" +
                "GROUP BY f.FILMS_ID, f.NAME, f.DESCRIPTION, f.RELEASEDATE, f.DURATION, f.RATING, f.GENRE_ID\n" +
                "ORDER BY total_likes DESC\n" +
                "LIMIT ?;", new RowMapper<Film>() {
            @Override
            public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
                Film film = new Film();
                film.setId(rs.getInt("id"));
                film.setName(rs.getString("name"));
                film.setDescription(rs.getString("description"));
                film.setReleaseDate(rs.getDate("releaseDate").toLocalDate());
                film.setDuration(rs.getInt("duration"));
                film.setMpa(getMpa(rs.getInt("rating")));
                film.setGenres(getGanresId(rs.getInt("id")));
                film.setRate(rs.getInt("total_likes"));
                return film;
            }
        }, quantityFilm);
        if (listIdFilm.size() == 0) {
            return (List<Film>) getFilm(quantityFilm);
        }
        return listIdFilm;
    }

    public List<Film> getFilmsPopular() {
        List<Film> listIdFilm = jdbcTemplate.query("SELECT f.FILMS_ID AS id, f.NAME, f.DESCRIPTION, f.RELEASEDATE, f.DURATION, f.RATING, f.GENRE_ID AS genre, COUNT(ul.ID_FILMS) AS total_likes\n" +
                "FROM FILMS f\n" +
                "LEFT JOIN USERS_LIKE ul ON ul.ID_FILMS = f.FILMS_ID\n" +
                "GROUP BY f.FILMS_ID, f.NAME, f.DESCRIPTION, f.RELEASEDATE, f.DURATION, f.RATING, f.GENRE_ID\n" +
                "ORDER BY total_likes DESC\n" +
                "LIMIT 10;", new RowMapper<Film>() {
            @Override
            public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
                Film film = new Film();
                film.setId(rs.getInt("id"));
                film.setName(rs.getString("name"));
                film.setDescription(rs.getString("description"));
                film.setReleaseDate(rs.getDate("releaseDate").toLocalDate());
                film.setDuration(rs.getInt("duration"));
                film.setMpa(getMpa(rs.getInt("rating")));
                film.setGenres(getGanresId(rs.getInt("id")));
                film.setRate(rs.getInt("total_likes"));
                return film;
            }
        });
        if (listIdFilm.size() == 0) {
            return getFilms();
        }
        return listIdFilm;
    }

    public void deleteFilm(Integer filmId) {
        jdbcTemplate.update("delete from film_genre where film_id = ? ", filmId);
        jdbcTemplate.update("delete from users_like where id_films = ? ", filmId);
        jdbcTemplate.update("delete from films where films_id = ? ", filmId);
    }

    public List<Film> getPopularFilmsByGenreAndYear(int genreId, int year) {
        List<Film> listIdFilm = jdbcTemplate.query("SELECT f.FILMS_ID AS id, f.NAME, f.DESCRIPTION, f.RELEASEDATE, f.DURATION, f.RATING, f.GENRE_ID AS genre, COUNT(ul.ID_FILMS) AS total_likes\n" +
                "FROM FILMS f\n" +
                "LEFT JOIN USERS_LIKE ul ON ul.ID_FILMS = f.FILMS_ID\n" +
                "LEFT JOIN FILM_GENRE fg ON f.FILMS_ID = fg.FILM_ID \n" +
                "WHERE fg.GENRE_ID = ? AND EXTRACT(YEAR FROM f.RELEASEDATE) = ?\n" +
                "GROUP BY f.FILMS_ID, f.NAME, f.DESCRIPTION, f.RELEASEDATE, f.DURATION, f.RATING, f.GENRE_ID\n" +
                "ORDER BY total_likes DESC\n", new RowMapper<Film>() {
            @Override
            public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
                Film film = new Film();
                film.setId(rs.getInt("id"));
                film.setName(rs.getString("name"));
                film.setDescription(rs.getString("description"));
                film.setReleaseDate(rs.getDate("releaseDate").toLocalDate());
                film.setDuration(rs.getInt("duration"));
                film.setMpa(getMpa(rs.getInt("rating")));
                film.setGenres(getGanresId(rs.getInt("id")));
                film.setRate(rs.getInt("total_likes"));
                return film;
            }
        }, genreId, year);
        return listIdFilm;
    }

    public List<Film> getPopularFilmsByGenre(int genreId) {
        List<Film> listIdFilm = jdbcTemplate.query("SELECT f.FILMS_ID AS id, f.NAME, f.DESCRIPTION, f.RELEASEDATE, f.DURATION, f.RATING, f.GENRE_ID AS genre, COUNT(ul.ID_FILMS) AS total_likes\n" +
                "FROM FILMS f\n" +
                "LEFT JOIN USERS_LIKE ul ON ul.ID_FILMS = f.FILMS_ID\n" +
                "LEFT JOIN FILM_GENRE fg ON f.FILMS_ID = fg.FILM_ID \n" +
                "WHERE fg.GENRE_ID = ?" +
                "GROUP BY f.FILMS_ID, f.NAME, f.DESCRIPTION, f.RELEASEDATE, f.DURATION, f.RATING, f.GENRE_ID\n" +
                "ORDER BY total_likes DESC\n", new RowMapper<Film>() {
            @Override
            public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
                Film film = new Film();
                film.setId(rs.getInt("id"));
                film.setName(rs.getString("name"));
                film.setDescription(rs.getString("description"));
                film.setReleaseDate(rs.getDate("releaseDate").toLocalDate());
                film.setDuration(rs.getInt("duration"));
                film.setMpa(getMpa(rs.getInt("rating")));
                film.setGenres(getGanresId(rs.getInt("id")));
                film.setRate(rs.getInt("total_likes"));
                return film;
            }
        }, genreId);
        return listIdFilm;
    }

    public List<Film> getPopularFilmsByYear(int year) {
        List<Film> listIdFilm = jdbcTemplate.query("SELECT f.FILMS_ID AS id, f.NAME, f.DESCRIPTION, f.RELEASEDATE, f.DURATION, f.RATING, f.GENRE_ID AS genre, COUNT(ul.ID_FILMS) AS total_likes\n" +
                "FROM FILMS f\n" +
                "LEFT JOIN USERS_LIKE ul ON ul.ID_FILMS = f.FILMS_ID\n" +
                "WHERE EXTRACT(YEAR FROM f.RELEASEDATE) = ? " +
                "GROUP BY f.FILMS_ID, f.NAME, f.DESCRIPTION, f.RELEASEDATE, f.DURATION, f.RATING, f.GENRE_ID\n" +
                "ORDER BY total_likes DESC\n", new RowMapper<Film>() {
            @Override
            public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
                Film film = new Film();
                film.setId(rs.getInt("id"));
                film.setName(rs.getString("name"));
                film.setDescription(rs.getString("description"));
                film.setReleaseDate(rs.getDate("releaseDate").toLocalDate());
                film.setDuration(rs.getInt("duration"));
                film.setMpa(getMpa(rs.getInt("rating")));
                film.setGenres(getGanresId(rs.getInt("id")));
                film.setRate(rs.getInt("total_likes"));
                return film;
            }
        }, year);
        return listIdFilm;
    }
}
