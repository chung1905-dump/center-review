package com.reviewtrungtam.webapp.review.repository;

import com.reviewtrungtam.webapp.center.entity.Center;
import com.reviewtrungtam.webapp.review.entity.Review;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewRepository extends CrudRepository<Review, Integer> {
    @Override
    <S extends Review> S save(S s);

    Optional<Review> findByIdAndStatus(Integer integer, Integer status);

    int countByCenter(Center center);
}
