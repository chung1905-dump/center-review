package com.reviewtrungtam.webapp.user.repository;

import com.reviewtrungtam.webapp.user.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
    Optional<User> findByUserName(String username);

    @Override
    <S extends User> S save(S s);
}
