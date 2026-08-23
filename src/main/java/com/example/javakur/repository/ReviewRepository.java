package com.example.javakur.repository;

import com.example.javakur.model.Review;

public class ReviewRepository extends BaseRepository<Review, Long> {
    public ReviewRepository() {
        super(Review.class);
    }
}
