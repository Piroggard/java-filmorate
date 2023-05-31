package ru.yandex.practicum.filmorate.model;

import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Film {
    @EqualsAndHashCode.Exclude
    private int id;
    @NonNull
    private String name;
    @NonNull
    private String description;
    @NonNull
    private LocalDate releaseDate;
    @NonNull
    private int duration;

    public Film(@NonNull String name, @NonNull String description, @NonNull LocalDate releaseDate, @NonNull int duration) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }
}
