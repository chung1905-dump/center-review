package com.reviewtrungtam.webapp.review.service;

import com.reviewtrungtam.webapp.center.entity.Center;
import com.reviewtrungtam.webapp.center.repository.CenterRepository;
import com.reviewtrungtam.webapp.center.service.CenterService;
import com.reviewtrungtam.webapp.general.config.Status;
import com.reviewtrungtam.webapp.general.exception.AppException;
import com.reviewtrungtam.webapp.review.entity.Review;
import com.reviewtrungtam.webapp.review.repository.ReviewRepository;
import com.reviewtrungtam.webapp.user.core.UserDetails;
import com.reviewtrungtam.webapp.user.entity.User;
import com.reviewtrungtam.webapp.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ReviewService {
    private static final int maxLevel = 5;

    private final ReviewRepository reviewRepository;

    private final CenterRepository centerRepository;

    private final CenterService centerService;

    private final UserService userService;

    @Autowired
    public ReviewService(
            ReviewRepository reviewRepository,
            CenterService centerService,
            CenterRepository centerRepository,
            UserService userService
    ) {
        this.reviewRepository = reviewRepository;
        this.centerService = centerService;
        this.centerRepository = centerRepository;
        this.userService = userService;
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
        prepareAuthor(review);
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

    public void validateNewReview(Review review) throws AppException {
        if (review.getDownVote() != 0 || review.getUpVote() != 0 || review.getPoint() != 0 ||
                review.getRating() < -2 || review.getRating() > 2 ||
                review.getComment() == null || review.getComment().isBlank()) {
            throw new AppException("Invalid Review");
        }
    }

    private void prepareAuthor(Review review) {
        if (userService.isAnonymous()) {
            review.setAnonymous(true);
        } else {
            User u = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
            review.setUser(u);
        }

        if (review.isAnonymous()) {
            review.setAuthorName(null);
        } else {
            review.setAuthorName(SecurityContextHolder.getContext().getAuthentication().getName());
        }
    }

    private void updateCenterPoint(Review review) {
        if (review.getRating() == 0) return;
        Center center = review.getCenter();
        List<Review> reviews = center.getReviewList();
        int sum = review.getRating(), count = 1;
        for (Review r : reviews) {
            if (r.getRating() == 0 || r.getStatus() != Status.ACTIVE) continue;
            sum += r.getRating();
            count++;
        }
        center.setPoint(sum / count);
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
