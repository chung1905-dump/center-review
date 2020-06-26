package com.reviewtrungtam.webapp.review.service;

import com.reviewtrungtam.webapp.center.entity.Center;
import com.reviewtrungtam.webapp.center.service.CenterService;
import com.reviewtrungtam.webapp.general.exception.AppException;
import com.reviewtrungtam.webapp.review.entity.Review;
import com.reviewtrungtam.webapp.review.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;

    private final CenterService centerService;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository, CenterService centerService) {
        this.reviewRepository = reviewRepository;
        this.centerService = centerService;
    }

    public void preSave(Review review, int centerId, String ip) throws AppException {
        if (centerId > 0) {
            prepareCenter(review, centerId);
        }
        if (ip != null && ip.length() > 0) {
            review.setIp(ip);
        }
        review.setStatus(review.getDefaultStatus());
    }

    public Review save(Review review) {
        return reviewRepository.save(review);
    }

    private void prepareCenter(Review review, int centerId) throws AppException {
        Center center = centerService.findActiveById(centerId);
        if (center == null) {
            throw new AppException("Center not found!");
        }
        review.setCenter(center);
    }
}
