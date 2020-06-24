package com.reviewtrungtam.webapp.center.repository;

import org.springframework.data.repository.CrudRepository;
import com.reviewtrungtam.webapp.center.entity.Center;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CenterRepository extends CrudRepository<Center, Integer> {
    @Override
    List<Center> findAll();

    Center findCenterBySlugNameEqualsAndIsActiveIsTrue(String slugName);

    Center findBySlugNameAndIsActiveIsTrue(String slugName);
}
