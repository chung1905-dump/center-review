package com.reviewtrungtam.webapp.review.service;

import com.reviewtrungtam.webapp.center.entity.Center;
import com.reviewtrungtam.webapp.center.repository.CenterRepository;
import com.reviewtrungtam.webapp.center.service.CenterService;
import com.reviewtrungtam.webapp.general.config.Status;
import com.reviewtrungtam.webapp.general.exception.AppException;
import com.reviewtrungtam.webapp.review.entity.Review;
import com.reviewtrungtam.webapp.review.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class ReviewService {
    private static final int maxLevel = 5;

    private final ReviewRepository reviewRepository;

    private final CenterRepository centerRepository;

    private final CenterService centerService;

    @Autowired
    public ReviewService(
            ReviewRepository reviewRepository,
            CenterService centerService,
            CenterRepository centerRepository
    ) {
        this.reviewRepository = reviewRepository;
        this.centerService = centerService;
        this.centerRepository = centerRepository;
    }

    public void preSave(Review review, int centerId, String ip) throws AppException {
        if (centerId > 0) {
            setCenterEntity(review, centerId);
        }
        if (ip != null && ip.length() > 0) {
            review.setIp(ip);
        }
        review.setStatus(review.getDefaultStatus());
        review.setRating(review.getRating() * 100);
    }

    public void setParent(Review review, Review parent) throws AppException {
        int level = parent.getLevel() + 1;
        if (level > maxLevel || !isAbleToReply(parent)) {
            throw new AppException("Cannot reply to this answer");
        }

        review.setParent(parent);
        review.setLevel(level);
    }

    public Set<Review> getReviews(Center center) throws AppException {
        Set<Review> reviews = new HashSet<>();

        return reviews;
    }

    public Review save(Review review) {
        reviewRepository.save(review);
        updateCenterPoint(review);
        return review;
    }

    private void updateCenterPoint(Review review) {
//        TODO: Optimize this
        Center center = review.getCenter();
        int total = center.getTotal() + review.getRating();
        int reviewNumber = center.getReviewList().size();
        int newPoint = total / reviewNumber;
        center.setPoint(newPoint);
        center.setTotal(total);
        centerRepository.save(center);
    }

    private boolean isAbleToReply(Review review) {
        return review.getStatus() != Status.ACTIVE;
    }

    private void setCenterEntity(Review review, int centerId) throws AppException {
        Center center = centerService.findActiveById(centerId);
        if (center == null) {
            throw new AppException("Center not found!");
        }
        review.setCenter(center);
    }
}
