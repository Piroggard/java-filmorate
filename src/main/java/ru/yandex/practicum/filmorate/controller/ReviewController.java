package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.ReviewService;

import java.util.List;

@RestController
@Slf4j
@AllArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping("/reviews")
    public Review createReview(@RequestBody Review review) {
        System.out.println("Работает");
        log.info("Добавление нового отзыва: {}", review);
        Review createdReview = reviewService.createReview(review);
        return createdReview;
    }

    @PutMapping("/reviews")
    public Review updateReview(@RequestBody Review review) {
        Review updatedReview = reviewService.updateReview(review);
        log.info("Редактирование уже имеющегося отзыва: {}", review);
        return updatedReview;
    }

    @DeleteMapping("/reviews/{id}")
    public void removeReview(@PathVariable("id") Integer id) {
        log.info("Удаление уже имеющегося отзыва по id: {}", id);
        reviewService.removeReview(id);
    }

    @GetMapping("/reviews/{id}")
    public Review getReviewById(@PathVariable("id") Integer id) {
        log.info("Получение отзыва по id: {}", id);
        return reviewService.getReviewById(id);
    }

    @GetMapping("/reviews")
    public List<Review> getAllReviews(
            //Этот параметр запроса не является обязательным
            @RequestParam(required = false) Integer filmId,
            //Если count не указан, будет использовано значение 10
            @RequestParam(required = false, defaultValue = "10") int count) {
        return reviewService.getAllReviews(filmId, count);
    }

    @PutMapping("/reviews/{id}/like/{userId}")
    public void likeReview(@PathVariable(name = "id") Integer reviewId,
                           @PathVariable(name = "userId") Integer userId) {
        reviewService.likeReview(reviewId, userId);
    }

    @PutMapping("/reviews/{id}/dislike/{userId}")
    public void dislikeReview(@PathVariable(name = "id") Integer reviewId,
                              @PathVariable(name = "userId") Integer userId) {
        reviewService.dislikeReview(reviewId, userId);
    }

    @DeleteMapping("/reviews/{id}/like/{userId}")
    public void removeLike(@PathVariable(name = "id") Integer reviewId,
                           @PathVariable(name = "userId") Integer userId) {
        reviewService.removeLike(reviewId, userId);
    }

    @DeleteMapping("/reviews/{id}/dislike/{userId}")
    public void removeDislike(@PathVariable(name = "id") Integer reviewId,
                              @PathVariable(name = "userId") Integer userId) {
        reviewService.removeDislike(reviewId, userId);
    }
}
