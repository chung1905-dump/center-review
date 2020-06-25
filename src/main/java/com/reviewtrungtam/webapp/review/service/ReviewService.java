package com.reviewtrungtam.webapp.review.service;

import com.reviewtrungtam.webapp.review.entity.Review;
import com.reviewtrungtam.webapp.review.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public void preSave(Review review, Set<String> errs) {
        review.setStatus(review.getDefaultStatus());
    }

    public Review save(Review review) {
        return reviewRepository.save(review);
    }
}
