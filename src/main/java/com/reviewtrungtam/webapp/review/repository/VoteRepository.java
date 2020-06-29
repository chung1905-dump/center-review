package com.reviewtrungtam.webapp.review.repository;

import com.reviewtrungtam.webapp.review.entity.Review;
import com.reviewtrungtam.webapp.review.entity.Vote;
import com.reviewtrungtam.webapp.user.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoteRepository extends CrudRepository<Vote, Integer> {
    @Override
    Vote save(Vote s);

    Vote findByUserAndReview(User user, Review review);
}
