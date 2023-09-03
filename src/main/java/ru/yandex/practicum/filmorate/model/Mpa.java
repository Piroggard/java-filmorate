package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class Mpa implements Comparable<Mpa> {
    public Mpa(Integer id) {
        this.id = id;
    }

    private Integer id;
    private String name;
    private String description;

    @Override
    public int compareTo(Mpa o) {
        return Integer.compare(this.id, o.getId());
    }
}
