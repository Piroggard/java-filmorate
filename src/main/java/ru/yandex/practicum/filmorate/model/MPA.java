package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MPA implements Comparable<MPA>{
    public MPA(Integer id) {
        this.id = id;
    }

    Integer id;
    String name;
    String descriptionMPA;

    @Override
    public int compareTo(MPA o) {
        return Integer.compare(this.id, o.getId());
    }
}
