package ru.yandex.practicum.filmorate.model;

import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Film implements Comparable<Film> {
    @EqualsAndHashCode.Exclude
    private int id;
    @NonNull
    private String name;
    @NonNull
    private String description;
    @NonNull
    private LocalDate releaseDate;
    private Set<Genres> genres;

    private MPA mpa;
    private int duration;
    private Set<Integer> usersLikeMovie = new HashSet<>();




    private int rate;


    public Film(int id, @NonNull String name, @NonNull String description, @NonNull LocalDate releaseDate, Set<Genres> genre, int duration , MPA mpa) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.genres = genre;
        this.duration = duration;
        this.mpa = mpa;

    }
/*public void setUsersLikeMovie(Set<Integer> usersLikeMovie) {
        this.setLikes(usersLikeMovie.size());
        this.usersLikeMovie = usersLikeMovie;
    }

  /*  public void addLike(int id) {
        usersLikeMovie.add(id);
        setRate(getRate() + 1);
    }

    public void deleteLike(int id) {
        usersLikeMovie.remove(id);
        setRate(getRate() - 1);
    }*/



    @Override
    public int compareTo(Film o) {
        return Integer.compare(o.getRate(), this.rate);
    }
    public int compareByDi(Film o) {
        return Integer.compare(this.id , o.getId() ); // Сортировка по полю di от большего к меньшему
    }
    public Film(@NonNull String name, @NonNull String description, @NonNull LocalDate releaseDate, int duration) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }


}
