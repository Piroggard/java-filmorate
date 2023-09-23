package ru.yandex.practicum.filmorate.storage.review;

import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewStorage {
    Review createReview(Review newReview);

    Review updateReview(Review updatedReview);

    boolean removeReview(Integer deletedReviewId);

    Optional<Review> getReviewById(Integer reviewId);

    List<Review> getAllReviews(Integer filmId, int count);

    void likeReview(Integer reviewId, Integer userId);

    void dislikeReview(Integer reviewId, Integer userId);

    void removeLike(Integer reviewId, Integer userId);

    void removeDislike(Integer reviewId, Integer userId);

    boolean reviewExists(Integer reviewId);
}
