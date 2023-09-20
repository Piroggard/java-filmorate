package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;

public interface ReviewService {
    Review createReview(Review newReview);

    Review updateReview(Review updatedReview);

    void removeReview(Integer deletedReviewId);

    Review getReviewById(Integer reviewId);

    List<Review> getAllReviews(Integer filmId, int count);

    void likeReview(Integer reviewId, Integer userId);

    void dislikeReview(Integer reviewId, Integer userId);

    void removeLike(Integer reviewId, Integer userId);

    void removeDislike(Integer reviewId, Integer userId);

    void validateFilmId(Integer filmId);

    void validateUserId(Integer userId);

    void validateReviewId(Integer reviewId);

    void validateReviewIdAndUserId(Integer reviewId, Integer userId);

    void validateFilmIdAndUserId(Integer filmId, Integer userId);


}
