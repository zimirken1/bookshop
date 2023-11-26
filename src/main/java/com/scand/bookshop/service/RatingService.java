package com.scand.bookshop.service;

import com.scand.bookshop.entity.Book;
import com.scand.bookshop.entity.Rating;
import com.scand.bookshop.entity.User;
import com.scand.bookshop.repository.RatingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RatingService {
    private final RatingRepository ratingRepository;

    @Transactional
    public void addRating(Book book, User user, int value) {
        Optional<Rating> existingRating = ratingRepository.findByBookAndUser(book,user);
        if (existingRating.isPresent()) {
            existingRating.get().setRatingValue(value);
        } else {
            Rating rating = new Rating(null, book, user, value);
            ratingRepository.save(rating);
        }
    }

    public double calculateAverageRating(Book book) {
        if (book.getRatings().isEmpty()) {
            return 0.0;
        }

        double sum = book.getRatings().stream()
                .mapToInt(Rating::getRatingValue)
                .sum();
        return sum / book.getRatings().size();
    }
}
