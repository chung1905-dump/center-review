package com.reviewtrungtam.webapp.review.repository;

import com.reviewtrungtam.webapp.review.entity.Review;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends CrudRepository<Review, Integer> {
    @Override
    Review save(Review s);
}
