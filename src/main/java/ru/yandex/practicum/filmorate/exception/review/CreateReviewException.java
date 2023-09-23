package ru.yandex.practicum.filmorate.exception.review;

public class CreateReviewException extends RuntimeException {

    public CreateReviewException(String message, Throwable cause) {
        super(message, cause);
    }
}
