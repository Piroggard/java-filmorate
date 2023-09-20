package ru.yandex.practicum.filmorate.validation;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.Review;

@Component
public class ValidationReviews {
    public void validarionReviews (Review review) {
        if (review.getUserId() < 1 || review.getFilmId()<1) {
            System.out.println(1);
            throw new DataNotFoundException("Нет данных по указанным параметрам");
        }
        if (review.getContent() == null  || review.getIsPositive() == null) {
            throw new RuntimeException();
        }
    }
}
