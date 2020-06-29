package com.reviewtrungtam.webapp.center.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import com.reviewtrungtam.webapp.center.entity.Center;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CenterRepository extends CrudRepository<Center, Integer> {
    @Override
    List<Center> findAll();

    Center findBySlugNameAndStatus(String slugName, int status);

    Center findByIdAndStatus(int id, int status);

    @Query("SELECT c, SUM(r.rating) AS total FROM Center c " +
            "JOIN Review r ON r.center = c " +
            "WHERE c.id = ?1")
    Center findByIdAndTotalPoint(int id);
}
