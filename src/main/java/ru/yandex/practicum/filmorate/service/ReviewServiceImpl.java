package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.DataNotFoundException;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;
import ru.yandex.practicum.filmorate.storage.review.ReviewStorage;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ReviewServiceImpl implements ReviewService {
    private final ReviewStorage reviewStorage;
    private final FilmDbStorage filmStorage;
    private final UserDbStorage userStorage;

    @Override
    public Review createReview(Review newReview) {
        validateFilmIdAndUserId(newReview.getFilmId(), newReview.getUserId());
        return reviewStorage.createReview(newReview);
    }

    @Override
    public Review updateReview(Review updatedReview) {
        validateReviewId(updatedReview.getReviewId());
        return reviewStorage.updateReview(updatedReview);
    }

    @Override
    public void removeReview(Integer deletedReviewId) {
        validateReviewId(deletedReviewId);
        reviewStorage.removeReview(deletedReviewId);
    }

    @Override
    public Review getReviewById(Integer reviewId) {
        validateReviewId(reviewId);
        return reviewStorage.getReviewById(reviewId)
                .orElseThrow(() -> new DataNotFoundException("Отзыв с указанным ID не найден: " + reviewId));
    }

    @Override
    public List<Review> getAllReviews(Integer filmId, int count) {
        return reviewStorage.getAllReviews(filmId, count);
    }

    @Override
    public void likeReview(Integer reviewId, Integer userId) {
        validateReviewIdAndUserId(reviewId, userId);
        reviewStorage.likeReview(reviewId, userId);
    }

    @Override
    public void dislikeReview(Integer reviewId, Integer userId) {
        validateReviewIdAndUserId(reviewId, userId);
        reviewStorage.dislikeReview(reviewId, userId);
    }

    @Override
    public void removeLike(Integer reviewId, Integer userId) {
        validateReviewIdAndUserId(reviewId, userId);
        reviewStorage.removeLike(reviewId, userId);
    }

    @Override
    public void removeDislike(Integer reviewId, Integer userId) {
        validateReviewIdAndUserId(reviewId, userId);
        reviewStorage.removeDislike(reviewId, userId);
    }

    @Override
    public void validateFilmId(Integer filmId) {
        if (!filmStorage.filmExists(filmId)) {
            log.warn("Фильм с id {} не найден", filmId);
            throw new DataNotFoundException("Фильм с id " + filmId + " не найден.");
        }
    }

    @Override
    public void validateUserId(Integer userId) {
        if (!userStorage.userExists(userId)) {
            log.warn("Пользователь с id {} не найден", userId);
            throw new DataNotFoundException("Пользователь с id " + userId + " не найден.");
        }
    }

    @Override
    public void validateReviewId(Integer reviewId) {
        if (!reviewStorage.reviewExists(reviewId)) {
            log.warn("Отзыв с id {} не найден", reviewId);
            throw new DataNotFoundException("Отзыв с id " + reviewId + " не найден.");
        }
    }

    @Override
    public void validateReviewIdAndUserId(Integer reviewId, Integer userId) {
        validateReviewId(reviewId);
        validateUserId(userId);
    }

    @Override
    public void validateFilmIdAndUserId(Integer filmId, Integer userId) {
        validateFilmId(filmId);
        validateUserId(userId);
    }
}
