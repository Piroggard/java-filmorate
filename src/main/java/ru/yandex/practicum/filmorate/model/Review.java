package ru.yandex.practicum.filmorate.model;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder(toBuilder = true)
@Data
public class Review {
    public Review(String content, Boolean isPositive, Integer userId, Integer filmId) {
        this.content = content;
        this.isPositive = isPositive;
        this.userId = userId;
        this.filmId = filmId;
    }

    private Integer reviewId;
    private String content;
    private Boolean isPositive;
    private Integer userId;
    private Integer filmId;
    private Integer useful;
}
