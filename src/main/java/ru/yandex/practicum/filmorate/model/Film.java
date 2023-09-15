package ru.yandex.practicum.filmorate.model;

import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
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
    private Mpa mpa;
    private int duration;
    private Set<Integer> usersLikeMovie = new HashSet<>();

    private int rate;



    public Film(String name, String description, LocalDate releaseDate, int duration, int rate) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.rate = rate;
    }

    public Film(int id, @NonNull String name, @NonNull String description, @NonNull LocalDate releaseDate, Set<Genres> genre, int duration, Mpa mpa) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.genres = genre;
        this.duration = duration;
        this.mpa = mpa;
    }



    public Film(@NonNull String name, @NonNull String description, @NonNull LocalDate releaseDate, int duration) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }

    @Override
    public int compareTo(Film o) {
        return Integer.compare(o.getRate(), this.rate);
    }

    public int compareById(Film o) {
        return Integer.compare(this.id, o.getId());
    }
}
